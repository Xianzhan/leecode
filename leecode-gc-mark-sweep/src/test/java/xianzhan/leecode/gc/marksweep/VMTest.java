package xianzhan.leecode.gc.marksweep;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VMTest {

    private VM vm;

    @BeforeEach
    public void before() {
        vm = VM.newVM();
    }

    @Test
    public void test1() {
        System.out.println("Test 1: Objects on stack are preserved.");
        vm.pushInt(1);
        vm.pushInt(2);

        vm.gc();
        Assertions.assertEquals(2, vm.numObjs, "Should have preserved objects.");
        vm.freeVM();
    }

    @Test
    public void test2() {
        System.out.println("Test 2: Unreached objects are collected.");
        vm.pushInt(1);
        vm.pushInt(2);
        vm.pop();
        vm.pop();

        vm.gc();
        Assertions.assertEquals(0, vm.numObjs, "Should have collected objects.");
        vm.freeVM();
    }

    @Test
    public void test3() {
        System.out.println("Test 3: Reach nested objects.");
        vm.pushInt(1);
        vm.pushInt(2);
        vm.pushPair();
        vm.pushInt(3);
        vm.pushInt(4);
        vm.pushPair();
        vm.pushPair();

        vm.gc();
        Assertions.assertEquals(7, vm.numObjs, "Should have reached objects.");
        vm.freeVM();
    }

    @Test
    public void test4() {
        System.out.println("Test 4: Handle cycles.");
        vm.pushInt(1);
        vm.pushInt(2);
        Obj a = vm.pushPair();
        vm.pushInt(3);
        vm.pushInt(4);
        Obj b = vm.pushPair();

        a.tail = b;
        b.tail = a;

        vm.gc();
        Assertions.assertEquals(4, vm.numObjs, "Should have collected objects.");
        vm.freeVM();
    }

    @Test
    public void testPerf() {
        System.out.println("Performance Test.");

        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 20; j++) {
                vm.pushInt(i);
            }

            for (int k = 0; k < 20; k++) {
                vm.pop();
            }
        }
        vm.freeVM();
    }
}
