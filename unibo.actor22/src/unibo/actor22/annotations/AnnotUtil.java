package unibo.actor22.annotations;

import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import unibo.actor22.Qak22Context;
import unibo.actor22comm.ProtocolInfo;
import unibo.actor22comm.ProtocolType;
import unibo.actor22comm.utils.ColorsOut;
 

public class AnnotUtil {
/*
-------------------------------------------------------------------------------
RELATED TO Actor22
-------------------------------------------------------------------------------
*/
	
	public static void createActorLocal(Object element) {
        Class<?> clazz = element.getClass();
        ActorLocal a = clazz.getAnnotation(ActorLocal.class);
        if (a != null) {
            for( int i=0; i<a.name().length; i++) {
                String name = a.name()[i];
                Class  impl = a.implement()[i];
                try {
                    impl.getConstructor( String.class ).newInstance( name );
                    ColorsOut.outappl( "CREATED LOCAL ACTOR: "+ name, ColorsOut.MAGENTA );
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
	}
	public static void  createProxyForRemoteActors(Object element) {
        Class<?> clazz = element.getClass();
        ActorRemote a = clazz.getAnnotation(ActorRemote.class);
        if (a != null) {
            for (int i = 0; i < a.name().length; i++) {
                String name = a.name()[i];
                String host = a.host()[i];
                String port = a.port()[i];
                String protocol = a.protocol()[i];
                Qak22Context.setActorAsRemote(name, port, host, ProtocolInfo.getProtocol(protocol));
                ColorsOut.outappl(
                        "CREATE REMOTE ACTOR PROXY:" + name + " host:" + host + " port:" + port
                                + " protocol:" + protocol, ColorsOut.MAGENTA);
            }
        }
    }

    public static Map<String, Context> getRemoteContexts(Object element) {
        Map<String, Context> out = new HashMap<>();
        Class<?> clazz            = element.getClass();
        Annotation[] annotations  = clazz.getAnnotations();
        Context[] remoteContexts = element.getClass().getAnnotationsByType(Context.class);
        for (Context rc : remoteContexts) {
            String name     = rc.name();
            String host     = rc.host();
            String port     = rc.port();
            ProtocolType protocol = rc.protocol();
            out.put(name, rc);
//            ColorsOut.outappl("Registered remote context " + name+ " at "
//                            + String.format("%s//%s:%s", protocol, host, port), ColorsOut.BLUE);
        }
        return out;
    }

    public static void handleRepeatableActorDeclaration(Object element) {
        Class<?> clazz = element.getClass();
        Map<String, Context> remoteContexts = null;
        Actor[] actorAnnotations = clazz.getAnnotationsByType(Actor.class);
        for (Actor actor : actorAnnotations) {
            String name = actor.name();
            boolean isLocal = actor.local();
            if (isLocal) {
                Class implement = actor.implement();
                if (implement.equals(void.class))
                    throw new IllegalArgumentException("@Actor: Local actor needs a class specification");
                try {
                    implement.getConstructor(String.class).newInstance(name);
                    ColorsOut.outappl( "Qak22Context | CREATED LOCAL ACTOR: "+ name, ColorsOut.MAGENTA );
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
            } else {
                String remoteContextName = actor.contextName();
                if (remoteContextName.equals(""))
                    throw new IllegalArgumentException("@Actor: Remote actor needs a remoteContextName");
                if (remoteContexts == null)
                    remoteContexts = AnnotUtil.getRemoteContexts(element);
                if (!remoteContexts.containsKey(remoteContextName)) {
                    throw new IllegalArgumentException("@Actor: remoteContextName invalid, Available:" + remoteContexts.keySet());
                }

                Context rc = remoteContexts.get(remoteContextName);
                Qak22Context.setActorAsRemote(name, rc.port(), rc.host(), rc.protocol());
                ColorsOut.outappl( "Qak22Context | SET REMOTE ACTOR: "+ name, ColorsOut.MAGENTA );
            }
        }
    }
	
/*
-------------------------------------------------------------------------------
RELATED TO PROTOCOLS
-------------------------------------------------------------------------------
 */
 
     public static ProtocolInfo checkProtocolConfigFile( String configFileName ) {
        try {
            System.out.println("IssAnnotationUtil | checkProtocolConfigFile configFileName=" + configFileName);
            FileInputStream fis = new FileInputStream(configFileName);
            Scanner sc = new Scanner(fis);
            String line = sc.nextLine();
            //System.out.println("IssAnnotationUtil | line=" + line);
            String[] items = line.split(",");

            String protocol = AnnotUtil.getProtocolConfigInfo("protocol", items[0]);
            System.out.println("IssAnnotationUtil | protocol=" + protocol);

            String url = AnnotUtil.getProtocolConfigInfo("url", items[1]);
            //System.out.println("IssAnnotationUtil | url=" + url);
             return null;
        } catch (Exception e) {
            System.out.println("IssAnnotationUtil | WARNING:" + e.getMessage());
            return null;
        }
    }

    //Quite bad: we will replace with Prolog parser
    public static String getProtocolConfigInfo(String functor, String line){
        Pattern pattern = Pattern.compile(functor);
        Matcher matcher = pattern.matcher(line);
        String content = null;
        if(matcher.find()) {
            int end = matcher.end() ;
            content = line.substring( end, line.indexOf(")") )
                    .replace("\"","")
                    .replace("(","").trim();
        }
        return content;
    }


//    /*
//-------------------------------------------------------------------------------
//RELATED TO ROBOT MOVES
//-------------------------------------------------------------------------------
// */
// 
// 
//    //Used also by IssArilRobotSupport
//    public static boolean checkRobotConfigFile(
//        String configFileName, HashMap<String, Integer> mvtimeMap){
//        try{
//            //spec( htime( 100 ),  ltime( 300 ), rtime( 300 ),  wtime( 600 ), wstime( 600 ) ).
//            //System.out.println("IssAnnotationUtil | checkRobotConfigFile configFileName=" + configFileName);
//            FileInputStream fis = new FileInputStream(configFileName);
//            Scanner sc = new Scanner(fis);
//            String line = sc.nextLine();
//            //System.out.println("IssAnnotationUtil | checkRobotConfigFile line=" + line);
//            String[] items = line.split(",");
//            mvtimeMap.put("h", getRobotConfigInfo("htime", items[0] ));
//            mvtimeMap.put("l", getRobotConfigInfo("ltime", items[1] ));
//            mvtimeMap.put("r", getRobotConfigInfo("rtime", items[2] ));
//            mvtimeMap.put("w", getRobotConfigInfo("wtime", items[3] ));
//            mvtimeMap.put("s", getRobotConfigInfo("stime", items[4] ));
//            //System.out.println("IssAnnotationUtil | checkRobotConfigFile ltime=:" + mvtimeMap.get("l"));
//            return true;
//        } catch (Exception e) {
//            System.out.println("IssAnnotationUtil | checkRobotConfigFile WARNING:" + e.getMessage());
//            return false;
//        }
//
//    }
//
//    protected static Integer getRobotConfigInfo(String functor, String line){
//        Pattern pattern = Pattern.compile(functor);
//        Matcher matcher = pattern.matcher(line);
//        String content = "0";
//        if(matcher.find()) {
//            int end = matcher.end() ;
//            content = line.substring( end, line.indexOf(")") )
//                    .replace("\"","")
//                    .replace("(","").trim();
//            //System.out.println("IssAnnotationUtil | getRobotConfigInfo functor=" + functor + " v=" + Integer.parseInt(content));
//        }
//        return Integer.parseInt( content );
//    }
//
 
}
