package com.blazingkin.interpreter.executor.astnodes;

import java.math.BigInteger;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.BinaryNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.parser.SyntaxException;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.VariableTypes;

public class BitwiseAndNode extends BinaryNode {


    public BitwiseAndNode(ASTNode[] args) throws SyntaxException {
        super(Operator.ExclusiveOr , args);
		if (args.length != 2){
			throw new SyntaxException("Bitwise and did not have 2 arguments");
		}
    }

    @Override
    public Value execute(Context c) throws BLZRuntimeException {
        Value l = args[0].execute(c);
        Value r = args[1].execute(c);
        if (l.type == VariableTypes.Integer && r.type == VariableTypes.Integer) {
            BigInteger lI = (BigInteger) l.value;
            BigInteger rI = (BigInteger) r.value;
            return Value.integer(lI.and(rI));
        }
        if (l.type != VariableTypes.Integer) {
            throw new BLZRuntimeException(l + " cannot be and-ed as it is not an integer (" + l.type + " instead)");    
        }
        throw new BLZRuntimeException(r + " cannot be and-ed as it is not an integer (" + r.type + " instead)");
    }

}