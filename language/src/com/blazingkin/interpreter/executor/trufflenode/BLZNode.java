package com.blazingkin.interpreter.executor.trufflenode;

import com.blazingkin.interpreter.variables.Value;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;;

@NodeInfo(language = "blz", description = "abstract node for all blz nodes")
public abstract class BLZNode extends Node {

    public abstract Value evaluate(VirtualFrame frame); 
 
}