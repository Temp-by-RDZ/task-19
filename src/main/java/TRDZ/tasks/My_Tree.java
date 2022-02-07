package TRDZ.tasks;

import java.util.Stack;

public class My_Tree<Type extends Comparable <? super Type>> implements Tree<Type> {
	My_Node<Type> root;
	int lyer;
	int lyer_max;
	int size;

	public My_Tree() {
		this.lyer_max = 2;
		}

	public My_Tree(int lyer_max) {
		this.lyer_max = Math.min(lyer_max, 7);
		}

//region Методы

	@Override
	public boolean contains(Type value) {
		My_Tree<Type>.NodeAndParent nodeAndParent = Find(value);
		return nodeAndParent.current != null;
		}

	private NodeAndParent Find(Type value) {
		My_Node<Type> current = root;
		My_Node<Type> parent = null;
		lyer=0;
		while (current != null) {
			if (current.item.equals(value)) {
				return new My_Tree<Type>.NodeAndParent(current, parent);
				}
			parent = current;
			lyer++;
			if (current.is_ToLeft(value)) current = current.branch_left;
			else current = current.branch_right;
			}
		return new My_Tree<Type>.NodeAndParent(null, parent);
		}

	@Override
	public boolean add(Type value) {
		NodeAndParent nodeAndParent = Find(value);
		if (nodeAndParent.current != null)  {nodeAndParent.current.repeats++; return false;}
		if (lyer>=lyer_max) return false;
		My_Node<Type> parent = nodeAndParent.parent;
		My_Node<Type> node = new My_Node<>(value);
		if (is_Empty()) root = node;
		else if (parent.is_ToLeft(value)) parent.branch_left=node;
		else parent.branch_right=node;
		size++;
		return true;
		}

	@Override
	public boolean remove(Type value) {
		My_Tree<Type>.NodeAndParent nodeAndParent = Find(value);
		My_Node<Type> parent = nodeAndParent.parent;
		My_Node<Type> removed = nodeAndParent.current;
		if (removed == null) return false;
		if (removed.is_Leaf()) removeLeafNode(removed, parent);
		else if (removed.is_Single()) removeNodeWithOneChild(removed, parent);
		else removedNodeWithAllChildren(removed, parent);
		size--;
		return true;
		}

	private void removeLeafNode(My_Node<Type> removed, My_Node<Type> parent) {
		if (removed == root) root = null;
		else if (parent.is_ToLeft(removed.item)) parent.branch_left=null;
		else parent.branch_right=null;
		}
	private void removeNodeWithOneChild(My_Node<Type> removed, My_Node<Type> parent) {
		My_Node<Type> child = removed.branch_left != null ? removed.branch_left : removed.branch_right;
		if (removed == root) root = null;
		else if (parent.is_ToLeft(removed.item)) parent.branch_left=child;
		else parent.branch_right=child;
		}
	private void removedNodeWithAllChildren(My_Node<Type> removed, My_Node<Type> parent) {
		My_Node<Type> successor = getSuccessor(removed);
		if (removed == root) root = successor;
		else if (parent.is_ToLeft(removed.item)) parent.branch_left=successor;
		else parent.branch_right=successor;
		}

	private My_Node<Type> getSuccessor(My_Node<Type> removed) {
		My_Node<Type> successor = removed;
		My_Node<Type> parent = null;
		My_Node<Type> current = removed.branch_right;

		while (current != null) {
			parent = successor;
			successor = current;
			current = current.branch_left;
			}

		if (successor != removed.branch_right && parent != null) {
			parent.branch_left=successor.branch_right;
			successor.branch_right=removed.branch_right;
			}
		successor.branch_left=removed.branch_left;

		return successor;
		}

	@Override
	public boolean is_Empty() {
		return size == 0;
		}

	public boolean isBalanced(My_Node<Type> node, int pref) {
		return (node == null) ||					//Мы в конце или...
			isBalanced(node.branch_left,pref) &&	//отправляемся проверять древо в левой ветви
			isBalanced(node.branch_right,pref) &&	//отправляемся проверять древо в правой ветви
			(Math.abs(height(node.branch_left) - height(node.branch_right)) <= pref);	//разница глубин не выше погрешности
		}
	private int height(My_Node<Type>  node) {
		return node == null ? 0 : 1 + Math.max(height(node.branch_left), height(node.branch_right));
		}

	@Override
	public void traverse(TraversMode mode) {
		switch (mode) {
		case PRE_ORDER:
			preOrder(root); //прямой
			break;
		case IN_ORDER:
			inOrder(root); //центированный
			break;
		case POST_ORDER:
			postOrder(root); //обратный
			break;
			}
		System.out.println();
		}

	private void preOrder(My_Node<Type> current) {
		if (current == null) return;
		System.out.print(current.item + " ");
		preOrder(current.branch_left);
		preOrder(current.branch_right);
		}
	private void inOrder(My_Node<Type> current) {
		if (current == null) return;
		inOrder(current.branch_left);
		System.out.print(current.item + " ");
		inOrder(current.branch_right);
		}
	private void postOrder(My_Node<Type> current) {
		if (current == null) return;
		postOrder(current.branch_left);
		postOrder(current.branch_right);
		System.out.print(current.item + " ");
		}

	public void display() {
		Stack<My_Node<Type>> globalStack = new Stack<>();
		globalStack.push(root);
		int nBlanks = 64;
		boolean isRowEmpty = false;

		System.out.println(".................................................................................................................................");
		while (!isRowEmpty) {
		Stack<My_Node<Type>> localStack = new Stack<>();

		isRowEmpty = true;
		for (int i = 0; i < nBlanks; i++) {
			System.out.print(" ");
			}

		while (!globalStack.isEmpty()) {
		My_Node<Type> tempNode = globalStack.pop();
		if (tempNode != null) {
		System.out.print(tempNode.item);
		localStack.push(tempNode.branch_left);
		localStack.push(tempNode.branch_right);
		if (tempNode.branch_left != null || tempNode.branch_right != null) {
		isRowEmpty = false;
		}
		} else {
		System.out.print("--");
		localStack.push(null);
		localStack.push(null);
		}
		for (int j = 0; j < nBlanks * 2 - 2; j++) {
		System.out.print(" ");
		}
		}

		System.out.println();

		while (!localStack.isEmpty()) {
		globalStack.push(localStack.pop());
		}

		nBlanks /= 2;
		}
		System.out.println(".................................................................................................................................");
		}

//endregion

//region Субклассы

	private class NodeAndParent {
		private final My_Node<Type> current;
		private final My_Node<Type> parent;

		public NodeAndParent(My_Node<Type> current, My_Node<Type> parent) {
			this.current = current;
			this.parent = parent;
			}
		}

	private static class My_Node<Type extends Comparable<? super Type>> {
		int repeats;
		Type item;
		My_Node<Type> branch_left;
		My_Node<Type> branch_right;

		private My_Node(Type item) {
			this.item=item;
			}

		private My_Node(Type item, My_Node<Type> left, My_Node<Type> right) {
			this(item);
			this.branch_left = left;
			this.branch_right = right;
			}

		public boolean is_ToLeft(Type over) {
			return item.compareTo(over)  > 0;
			}

		public boolean is_Leaf() {
			return branch_left == null && branch_right == null;
			}

		public boolean is_Single() {
			return branch_left != null ^ branch_right != null;
			}

		}

//endregion
	}
