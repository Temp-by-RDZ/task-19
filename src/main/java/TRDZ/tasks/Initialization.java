package TRDZ.tasks;

import java.util.ArrayList;

public class Initialization {
	static int deep = 4; //Глубина генерированных деревьев

	public static void main(String[] args) {
		ArrayList<My_Tree<Integer>> trees = new ArrayList<>();
		constr(trees);
		analyze(trees,"идеально ",0);
		analyze(trees,"",1);
		}

	public static void analyze(ArrayList<My_Tree<Integer>> getter,String prevS, int prevV) {
		int count=0;
		for (My_Tree<Integer> tree : getter ) {
			if (tree.isBalanced(tree.root,prevV)) {System.out.println("\n "+prevS+"сбалансированное дерево"); count++;}
			else System.out.println("\nне "+prevS+"сбалансированное древо");
			tree.display();
			}
		System.out.println("Количество "+prevS+"сбалансированных деревьев "+count+"/20 что является "+(count*100/20)+"%");
		}

	public static void linear(My_Tree<Integer> getter) {
		getter.traverse(Tree.TraversMode.IN_ORDER);
		getter.traverse(Tree.TraversMode.PRE_ORDER);
		getter.traverse(Tree.TraversMode.POST_ORDER);
		}

	public static ArrayList<My_Tree<Integer>> constr(ArrayList<My_Tree<Integer>> getter) {
		for (int i = 0; i < 20; i++) {
			My_Tree<Integer> temp = new My_Tree<>(deep);
			generate(temp,25-i);
			getter.add(temp);
			}
		return getter;
		}

	public static My_Tree<Integer> generate(My_Tree<Integer> getter, int total) {
		for (int i = 0; i < total; i++) getter.add(-25+(int)(Math.random()*26));
		return getter;
		}
	}


interface Tree<E extends Comparable<? super E>> {
	enum TraversMode {IN_ORDER, PRE_ORDER, POST_ORDER}
	boolean contains(E value);
	boolean add(E value);
	boolean remove(E value);
	boolean is_Empty();
	void display();
	void traverse(TraversMode mode);
	}
