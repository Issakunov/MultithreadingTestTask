package jam.workspace.service;

public class FunctionServiceImpl implements FunctionService{

    @Override
    public String findFactorial(long input) {
        int i,fact=1;
        for(i=1;i<=input;i++){
            fact=fact*i;
        }
        return input + " = " + fact;
    }
}
