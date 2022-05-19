%====================================================================================
% basicrobot description   
%====================================================================================
context(ctxbasicrobot, "10.201.10.78",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "it.unibo.basicrobot.Basicrobot").
  qactor( mock_console, ctxbasicrobot, "it.unibo.mock_console.Mock_console").
