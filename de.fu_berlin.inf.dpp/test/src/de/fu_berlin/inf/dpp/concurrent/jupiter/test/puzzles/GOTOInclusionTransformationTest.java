package de.fu_berlin.inf.dpp.concurrent.jupiter.test.puzzles;

import de.fu_berlin.inf.dpp.concurrent.jupiter.InclusionTransformation;
import de.fu_berlin.inf.dpp.concurrent.jupiter.Operation;
import de.fu_berlin.inf.dpp.concurrent.jupiter.internal.text.DeleteOperation;
import de.fu_berlin.inf.dpp.concurrent.jupiter.internal.text.GOTOInclusionTransformation;
import de.fu_berlin.inf.dpp.concurrent.jupiter.internal.text.InsertOperation;
import de.fu_berlin.inf.dpp.concurrent.jupiter.internal.text.SplitOperation;
import de.fu_berlin.inf.dpp.concurrent.jupiter.test.util.JupiterTestCase;

public class GOTOInclusionTransformationTest extends JupiterTestCase {

    protected InclusionTransformation inclusion = new GOTOInclusionTransformation();
    protected Operation insertOp = new InsertOperation(3, "abc");
    protected Operation splitOp1 = new SplitOperation(new DeleteOperation(2,
        "234"), new DeleteOperation(3, "6"));
    protected Operation splitOp2 = new SplitOperation(insertOp,
        new InsertOperation(7, "ins"));
    protected Operation splitOp3 = new SplitOperation(insertOp,
        new InsertOperation(6, "ins"));

    public void testSplitInsertTransformation() {

        // User A:
        // 0123456
        Operation a1 = new DeleteOperation(2, "234");
        // 0156
        Operation a2 = new DeleteOperation(3, "6");
        // 015

        // User B:
        // 0123456
        Operation b1 = new InsertOperation(3, "abc");
        // 012abc3456

        // Transform Operations from A to be used by B:
        Operation newOp = inclusion.transform(new SplitOperation(a1, a2), b1,
            Boolean.TRUE);

        Operation expectedOp = new SplitOperation(new SplitOperation(
            new DeleteOperation(2, "2"), new DeleteOperation(5, "34")),
            new DeleteOperation(6, "6"));
        assertEquals(expectedOp, newOp);

        // Transform Operations from B to be used by A:
        newOp = inclusion.transform(b1, new SplitOperation(a1, a2),
            Boolean.TRUE);

        // now position 2 but origin is 3
        expectedOp = new InsertOperation(2, "abc", 3);

        assertEquals(expectedOp, newOp);
    }

    public void testSplitSplitTransformation() {

        // User A:
        // 0123456
        Operation a1 = new DeleteOperation(2, "234");
        // 0156
        Operation a2 = new DeleteOperation(3, "6");
        // 015

        // User B:
        // 0123456
        Operation b1 = new InsertOperation(3, "abc");
        // 012abc3456
        Operation b2 = new InsertOperation(7, "ins");
        // 012abc3ins456

        SplitOperation a = new SplitOperation(a1, a2);
        SplitOperation b = new SplitOperation(b1, b2);

        // Result of both operation:
        // 01abcins5

        { // User B perspective:
            Operation newOp = inclusion.transform(a, b, Boolean.TRUE);
            Operation expectedOp = new SplitOperation(new SplitOperation(
                new DeleteOperation(2, "2"), new DeleteOperation(5, "3")),
                new SplitOperation(new DeleteOperation(8, "4"),
                    new DeleteOperation(9, "6")));
            assertEquals(expectedOp, newOp);
        }

        { // User A perspective:
            Operation newOp = inclusion.transform(b, a, Boolean.TRUE);
            Operation expectedOp = new SplitOperation(new InsertOperation(2,
                "abc", 3), new InsertOperation(5, "ins", 7));
            assertEquals(expectedOp, newOp);
        }
    }

    public void assertEquals(Operation op1, Operation op2) {
        assertEquals(op1.getTextOperations(), op2.getTextOperations());
    }

    public void testSplitSplitTransformation2() {

        // User A:
        // 0123456
        Operation a1 = new DeleteOperation(2, "234");
        // 0156
        Operation a2 = new DeleteOperation(3, "6");
        // 015

        // User B:
        // 0123456
        Operation b1 = new InsertOperation(3, "abc");
        // 012abc3456
        Operation b2 = new InsertOperation(6, "ins");
        // 012abcins3456

        SplitOperation a = new SplitOperation(a1, a2);
        SplitOperation b = new SplitOperation(b1, b2);

        // Result of both operation:
        // 01abcins5

        { // User B perspective:
            Operation newOp = inclusion.transform(a, b, Boolean.TRUE);
            Operation expectedOp = new SplitOperation(new DeleteOperation(2,
                "2"), new SplitOperation(new DeleteOperation(8, "34"),
                new DeleteOperation(9, "6")));
            assertEquals(expectedOp, newOp);
        }

        { // User A perspective:
            Operation newOp = inclusion.transform(b, a, Boolean.TRUE);
            Operation expectedOp = new SplitOperation(new InsertOperation(2,
                "abc", 3), new InsertOperation(5, "ins", 6));
            assertEquals(expectedOp, newOp);
        }
    }

    public void testReplaceTransformation() {

        Operation replace1 = new SplitOperation(new DeleteOperation(3, "def"),
            new InsertOperation(3, "345"));
        Operation replace2 = new SplitOperation(new InsertOperation(1, "123"),
            new DeleteOperation(4, "bcd"));
        Operation newOp = inclusion.transform(replace1, replace2, Boolean.TRUE);
        Operation expectedOp = new SplitOperation(new DeleteOperation(4, "ef"),
            new InsertOperation(4, "345", 3));
        assertEquals(expectedOp, newOp);
    }
}
