package com.blazingkin.interpreter.executor.astnodes;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.expressionabstraction.ASTNode;
import com.blazingkin.interpreter.expressionabstraction.Operator;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

public class TryCatchNode extends ASTNode {

    private ASTNode mainBlock, errorBlock;
    private String catchBinding;
    public TryCatchNode(ASTNode mainBlock, ASTNode errorBlock, String catchBinding){
        this.mainBlock = mainBlock;
        this.errorBlock = errorBlock;
        this.catchBinding = catchBinding;
    }

    @Override
    public boolean canCollapse(){
        return false;
    }

    @Override
    public ASTNode collapse(){
        return this;
    }

    @Override
    public String getStoreName(){
        return null;
    }

    @Override
    public Operator getOperator(){
        return null;
    }

    @Override
    public Value execute(Context con) throws BLZRuntimeException {
        try {
            return mainBlock.execute(con);
        }catch(BLZRuntimeException exception){
            Value errorObject = exception.exceptionValue;
            if (errorObject == null) {
                errorObject = Value.string(exception.getMessage());
            }
            con.setValue(catchBinding, errorObject);
            return errorBlock.execute(con);
        }
    }

}