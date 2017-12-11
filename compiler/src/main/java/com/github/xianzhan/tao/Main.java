package com.github.xianzhan.tao;

import com.github.xianzhan.tao.analysis.exception.LexicalAnalysisException;
import com.github.xianzhan.tao.analysis.exception.SyntacticAnalysisException;
import com.github.xianzhan.tao.intermediate.IntermediateCodeExpression;
import com.github.xianzhan.tao.vm.VirtualMachine;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/10/14
 */
public class Main {

    public static void main(String[] args) {
        VirtualMachine vm = VirtualMachine.instance();

        try {
            FileReader reader = new FileReader("system/lib/Array.t");
            vm.run(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (LexicalAnalysisException e) {
            e.printStackTrace();
        } catch (SyntacticAnalysisException e) {
            e.printStackTrace();
        } catch (IntermediateCodeExpression intermediateCodeExpression) {
            intermediateCodeExpression.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
