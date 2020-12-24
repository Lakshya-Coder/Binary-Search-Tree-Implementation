import java.util.Iterator;

public class MainClass {
    public static void main(String[] args) {
        BinarySearchTree<Character> bst = new BinarySearchTree<>();

        bst.add('A');
        bst.add('B');
        bst.add('C');
        bst.add('D');
        bst.add('E');
        bst.add('F');
        bst.add('G');
        bst.add('H');
        bst.add('I');
        bst.add('J');
        bst.add('K');
        bst.add('L');

        System.out.println(bst.size());
        System.out.println(bst.isEmpty());
        System.out.println(bst.contains('A'));
        System.out.println(bst.height());

        Iterator<Character> it = bst.traverse(TreeTraversalOrder.PRE_ORDER);

        it.forEachRemaining(n -> System.out.print(n + " "));
    }
}
