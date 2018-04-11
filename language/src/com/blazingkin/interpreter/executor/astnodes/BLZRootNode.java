
package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.BLZLanguage;
import com.blazingkin.interpreter.executor.trufflenode.BLZNode;
import com.blazingkin.interpreter.variables.Value;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.RootNode;

@NodeInfo(language = "blz", description = "The root of all BLZ execution trees")
public class BLZRootNode extends RootNode {
    @Child private BLZNode root;

    public BLZRootNode(BLZLanguage language, FrameDescriptor frameDescriptor, BLZNode root){
         super(language, frameDescriptor);
         this.root = root;
    }
 
    public Value execute(VirtualFrame frame){
         return Value.nil(); 
    }

}