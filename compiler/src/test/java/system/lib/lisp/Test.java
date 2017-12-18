package system.lib.lisp;

import com.github.xianzhan.lisp.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Test {
    public boolean testFile(File file) throws IOException, SyntaxException, RunTimeException {
        FileInputStream fis = new FileInputStream(file);
        StringBuffer sb = new StringBuffer();
        while (true) {
            char c = (char) fis.read();
            if (c == '\n' || c == '\r') break;
            sb.append(c);
        }

        Parser p = new Parser();
        ArrayList<ASTree> tree = p.parse(fis);
        Env env = new Env();
        ASTree r = null;
        for (ASTree t : tree) {
            r = Lisp.eval(t, env);
        }
        return r.toString().equals(sb.toString());
    }

    private int ok_count = 0;
    private int fail_count = 0;

    private void testDir(File file) throws IOException, SyntaxException, RunTimeException {
        System.out.printf("=== %s ===\n", file.getName());
        // Reading directory contents
        File[] files = file.listFiles();
        for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
            String name = files[i].getName();
            if (name.endsWith(".lisp")) {
                System.out.print(name);
                boolean ok = this.testFile(files[i]);
                if (ok)
                    this.ok_count++;
                else {
                    this.fail_count++;
                    System.out.print(" Fail!");
                }
                System.out.println();
            }
        }
    }

    // http://www.jianshu.com/p/509505d3bd50
    // http://daiyuwen.freeshell.org/gb/rol/roots_of_lisp.html
    public static void main(String[] args) {
        File project = new File("./compiler/src/test/java/system/lib/lisp");
        File tf = new File(project, "tests");
        Test t = new Test();
        try {
            t.testDir(new File(tf, "func_test"));
            t.testDir(new File(tf, "program_test"));
            t.testDir(new File(tf, "."));
            System.out.printf("====\nOK %d/%d Fail\n", t.ok_count, t.fail_count);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SyntaxException e) {
            e.printStackTrace();
        } catch (RunTimeException e) {
            e.printStackTrace();
        }
    }
}
