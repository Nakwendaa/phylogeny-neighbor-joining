import java.util.*;

/**
 * Un noeud contient les informations suivantes:
 * <ol>
 * 		<li><code>prot</code> contient le nom de la protéine contenu dans le noeud (possible null si noeud interne)</li>
 * 		<li><code>seq</code> contient la séquence d'acides aminés associée à la protéine (possible null si noeud interne)</li>
 * 		<li><code>dist</code> représente la longueur de l'arête parente du noeud.</li>
 * 		<li><code>left</code> représente l'enfant gauche d'un noeud</li>
 * 		<li><code>right</code> représente l'enfant droit d'un noeud</li>
 * </ol>
 * @author Paul Chaffanet - CHAP23049307
 */
public class Node {
	
	private String prot;
	private String seq;
	private double dist;
	private Node left;
	private Node right;

	/* Constructeurs */
	public Node() {
		this.prot = null;
		this.seq = null;
		this.dist = 0;
		this.left = null;
		this.right = null;
	}
	
	public Node(String key) {
		this.prot = key;
		this.seq = null;
		this.left = null;
		this.right = null;
	}
	
	public Node(String key, double distance) {
		this.prot = key;
		this.seq = null;
		this.left = null;
		this.right = null;
		this.dist = distance;
	}
	
	public Node(String key, String value) {
		this.prot = key;
		this.seq = value;
		this.left = null;
		this.right = null;
	}
	
	public Node(String prot, String seq, double dist, Node left, Node right) {
		this.prot = null;
		this.seq = null;
		this.dist = 0;
		this.left = null;
		this.right = null;
	}
	
	
	/* Accesseurs */
	public double getDist() {
		return this.dist;
	}
	public String getSeq() {
		return this.seq;
	}
	
	public String getProt() {
		return this.prot;
	}
	
	public Node getLeftChild() {
		return this.left;
	}
	
	public Node getRightChild() {
		return this.right;
	}
	
	/**
	 * Méthode qui permet de vérifie si un noeud est une feuille ou non.
	 * 
	 * @return <code>true</code> si le noeud est une feuille
	 * 		   <code>false</code> sinon.
	 */
	public boolean isLeave() {
		return this.left == null && this.right == null;
	}
	
	public void addLeftChild(Node child) {
		this.left = child;
	}
	
	public void addRightChild(Node child) {
		this.right = child;
	}
	
	/**
	 * Ajoute un enfant à à gauche du noeud s'il n'a pas déjà d'enfant gauche, à droite sinon.
	 * 
	 * @param child
	 * 		  le noeud enfant que l'on veut ajouter
	 */
	public void addChild(Node child) {
		if (this.getLeftChild() == null)
			this.addLeftChild(child);
		else
			this.addRightChild(child);
	}
	
	public void addSeq(String seq) {
		this.seq = seq;
	}
	
	public void addDist(double d) {
		this.dist = d;
	}
	
	/**
	 * Compter le nombre de feuilles à partir du noeud <code>this</code>.
	 * @return
	 */
	public int countLeaves () {
		  if( this.left == null && this.right == null ) 
		    return 1;
		  else 
		    return this.left.countLeaves() + this.right.countLeaves();  
	}
	
	/**
	 * Obtenir les noeuds feuilles d'un noeud <code>this</code>.
	 * 
	 * @return une liste de noeuds
	 */
	public ArrayList<Node> leavesNode () {
		ArrayList<Node> leaves = new ArrayList<Node>();
		
		if( this.left == null && this.right == null ) 
		    leaves.add(this);
		else {
			leaves.addAll(this.left.leavesNode());
			leaves.addAll(this.right.leavesNode());
		}
		return leaves;
	}
	
	/**
	 * Obtenir les protéines situées aux feuilles d'un noeud <code>this</code>.
	 * 
	 * @return une liste contenant les protéines situées aux feuilles.
	 */
	public HashSet<String> leavesProt () {
		HashSet<String> leaves = new HashSet<String>();
		
		if( this.left == null && this.right == null ) 
		    leaves.add(this.prot);
		else {
			leaves.addAll(this.left.leavesProt());
			leaves.addAll(this.right.leavesProt());
		}
		return leaves;
	}
	
	/**
	 * Obtenir les séquences situées aux feuilles d'un noeud <code>this</code>.
	 * 
	 * @returnune liste contenant les séquences situées aux feuilles.
	 */
	public ArrayList<String> leavesSeq () {
		ArrayList<String> leaves = new ArrayList<String>();
		
		if( this.left == null && this.right == null ) 
		    leaves.add(this.seq);
		else {
			leaves.addAll(this.left.leavesSeq());
			leaves.addAll(this.right.leavesSeq());
		}
		return leaves;
	}
	
	/**
	 * Méthode récursive afin de pouvoir imprimer en post-ordre la protéine des noeuds. (possiblement null)
	 */
	public void postOrder() {
		if (this.getLeftChild() != null) {
			this.getLeftChild().postOrder();
		}
		if (this.getRightChild() != null) {
			this.getRightChild().postOrder();
		}
		System.out.println(this.getProt());
	}
	
	/**
	 * Retourne une copie d'instance d'un noeud
	 */
	public Node clone() {
		return new Node(this.prot, this.seq, this.dist, this.left, this.right);
	}
}
