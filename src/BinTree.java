import java.util.*;

/**
 * La structure de donnée d'arbre binaire est très simple.
 * Elle ne se constitue que d'un noeud racine. Il existe aussi un attribut bipartitions dont l'accès provoque le calcul de toutes les bipartitions de l'arbre.
 * Nous utiliserons très souvent des méthodes récursives du fait de leur simplicité d'implémentation.
 * 
 * @author Paul Chaffanet - CHAP23049307
 *
 */
public class BinTree {
	
	private Node root;
	private HashSet<HashSet<HashSet<String>>> bipartitions;
	
	/* Constructeurs */
	public BinTree() {
		this.root = null;
		this.bipartitions = null;
	}
	
	public BinTree(Node root) {
		this.root = root;
		this.bipartitions = null;
	}
	
	
	/* Getters */
	public Node getRoot() {
		return this.root;
	}

	/**
	 * Cette méthode permet d'obtenir les bipartitions non triviales d'un arbre binaire enraciné.
	 * Elle fait appel à la méthode partitionnerArbre() qui permet de calculer l'ensemble de biparitions non triviales
	 * d'un arbre binaire enraciné en mettant à jour cet ensemble à chaque accès à cet attribut.
	 * 
	 * @return Un ensemble de bipartitions correspondant aux protéines contenues aux feuilles. Possiblement vide.
	 * 
	 * @see #partitionnerArbre()		
	 */
	public HashSet<HashSet<HashSet<String>>> getBipartitions() {
		return this.partitionnerArbre();
	}
	
/// Méthodes pratiques sur les arbres binaires	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Cette méthode permet d'obtenir la liste des noeuds feuille d'un arbre binaire enraciné.
	 * 
	 * @see Node#leavesNode()
	 */
	public ArrayList<Node> getLeavesNode() {
		if (this.root == null)
			return new ArrayList<Node>();
		else
			return this.root.leavesNode();
	}
	
	/**
	 *  Cette méthode permet d'obtenir la liste des protéines contenues aux feuilles d'un arbre binaire enraciné.
	 *  
	 * @see Node#leavesProt()
	 */
	public HashSet<String> getLeavesProt() {
		if (this.root == null)
			return new HashSet<String>();
		else
			return this.root.leavesProt();
	}
	
	/**
	 *  Cette méthode permet d'obtenir la liste des séquences contenues aux feuilles d'un arbre binaire enraciné.
	 *  
	 * @see Node#leavesProt()
	 */
	public ArrayList<String> getLeavesSeq() {
		if (this.root == null)
			return new ArrayList<String>();
		else
			return this.root.leavesSeq();
	}
	
	/**
	 * Rechercher le noeud dans l'arbre enraciné binaire possédant la valeur {@code prot} passée en argument.
	 * 
	 * @param prot
	 * 		  une chaîne de caractère correspondant à la protéine du noeud recherché.
	 * @return un objet de type <code>Node</code> de l'arbre enraciné qui a comme attribut protéine {@code prot}.
	 * 
	 * @see #findNode(Node, String)
	 */
	public Node find(String prot) {
		if (this.root == null)
			return null;
		else
			return findNode(this.root, prot);
	}
	
	/**
	 * Permet de rechercher un noeud contenant la protéine <code>prot</code> en recherchant récursivement à gauche, puis à droite.
	 * 
	 * @param root
	 * 		  le noeud actuel sur lequel nous somme situés dans la récursion.
	 * @param prot
	 * 		  la protéine recherchée
	 * 
	 * @return un <code>Node</code> qui contient la protéine, <code>null</code> sinon.
	 */
	private Node findNode(Node root, String prot) {
		if (root != null ) {
			if (root.getProt() != null && root.getProt().equals(prot))
				return root;
			else {
				Node result = findNode(root.getLeftChild(), prot);
				if (result != null)
					return result;
				else
					result = findNode(root.getRightChild(), prot);
				return result;
			}
		}
		return null;
	}
	
	/**
	 * Obtenir le noeud parent de <code>node</code> dans l'arbre
	 * 
	 * @param node
	 * 		  le noeud dont on veut obtenir le parent
	 * 
	 * @return le noeud parent de <code>node</code>
	 * 
	 * @see #getParent(Node, Node)
	 */
	public Node getParent(Node node) {
		return getParent(this.root, node);
	}
	
	/**
	 * On recherche le parent d'un noeud de manière récursive sur son sous-arbre droit, puis sur son sous-arbre gauche.
	 * 
	 * @param parent
	 * 		  le noeud parent actuel dans l'arbre
	 * 
	 * @param node
	 * 		  le noeud dont on recherche le parent
	 * 
	 * @return Le noeud parent de <code>node</code>, <code>null</code> sinon.
	 */
	private Node getParent(Node parent, Node node) {
		if (parent == node || parent == null){
            return null;
		}
		else {
			if (parent.getLeftChild() == node || parent.getRightChild() == node)
				return parent;
			else {
				Node temp;
				temp = getParent(parent.getRightChild(), node);
				if (temp == null) {
					temp = getParent(parent.getLeftChild(), node);
				}
				return temp;
			}
		}
	}

/// Fin bloc ,éthodes pratiques sur les arbres binaires	///////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
/// Question 1: Parcours post-Ordre de l'arbre binaire enraciné /////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Cette méthode permet d'imprimer les protéines contenues aux feuilles d'un arbre binaire enraciné lors d'un parcours post-fixe de l'arbre.
	 * 
	 * @see Node#postOrder()
	 */
	public void postOrder() {
		if (this.root == null)
			return;
		else this.getRoot().postOrder();
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
/// Question 2: Transformation Newick --> BinTree//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Cette méthode permet d'extraire la position de la parenthèse fermante d'une chaîne de caractères commençant
	 * par une parenthèse ouvrante.
	 * 
	 * @param str
	 * 		une chaîne de caractères de la forme "(PCDHA1_Humain, ORJ23_Humain)" ou "()" ou "((PCDHA1_Humain, ORJ23_Humain))"
	 * 
	 * @return
	 * 		Position i de la parenthèse fermante de la chaîne str par rapport à la parenthèse ouvrante str[0].	
	 */
	private static int positionParFermante (String str) {
			
	/* Vérification du format de str:
			- on vérifie que str n'est pas une chaîne vide;
	 		- on vérifie que str a une parenthèse ouvrante en premier caractèrere; */
			
		if (str == null || !str.substring(0,1).equals("(")) {
			throw new IllegalArgumentException("Le noeud de Newick n'est pas au bon format");
		}
			
		/* La variable j signifie que nous avons vu une parenthèse ouvrante pour le moment qui n'est pas encore fermée. */
		int j = 1;
			
		/* On commence après la première parenthèse ouvrante. Par exemple pour "(PCDHA1_Humain, ORJ23_Humain)",
		 * on commence à "PCDHA1_Humain, ORJ23_Humain)" */
		for (int i = 1; i < str.length(); i++) {
			switch(str.substring(i, i + 1)) {
				
			case "(":
					j++;
					continue;
						
			case ")":
					if (--j == 0)
						return i;
						
			default:
					continue;	
			}
		}
			
		/* Si la méthode n'a pas pu retourner de position pour la parenthèse fermante, cela signifie qu'il n'y pas
		   de parenthèse fermante dans la chaîne passée en argument de la méthode. On lève donc une exception pour
		   signifie que la chaîne str n'est pas au bon format.*/  
		throw new IllegalArgumentException("La chaîne de Newick est dans un format incorrect.");
	}
		
	/**
	 * Cette méthode permet d'instancier un arbre binaire enraciné <code>BinTree</code> à partir d'une chaîne en format Newick valide.
	 * 
	 * @param newickTree
     * 		  une chaîne sous format valide Newick
	 * 
	 * @return <code>BinTree</code> un arbre binaire enraciné qui représente la chaîne de Newick.
	 * 
	 * @throws IllegalArgumentException si la chaîne n'est pas au format Newick valide
	 */
	public static BinTree fromNewickToTree(String newickTree) {
			
		/* On vérifie que la chaîne entrée en argument respecte le format d'une chaîne de Newick:
		 * 	1. Une longueur d'au moins 3.
		 * 	2. Le premier caractère doit être "(". 
		 *  3. L'avant-dernier caractère doit être ")".
		 *  4. Le dernier caractère doit être ";" pour signifier la fin de la chaîne de Newick. */
		if (newickTree == null || newickTree.length() < 3 || !newickTree.substring(0, 1).concat(newickTree.substring(newickTree.length() - 2, newickTree.length())).equals("();"))
			throw new IllegalArgumentException("L'arbre newick n'est pas au bon format");

		/* Si la chaîne est valide, alors on peut commencer à itérer à partir de la racine du nouvel arbre
		 * pour procéder à l'insertion des enfants */
		BinTree tree = new BinTree(new Node());
		fromNewickToTreeIter(tree.getRoot(), newickTree);
		return tree;
	}
		
	/**
	 * Cette méthode permet d'ajouter les noeuds enfants correspondants aux noeuds enfants de la chaîne de Newick spécifiée.
	 * 	 
	 * @param root un noeud pour lequel on veut ajouter ses enfants selon la chaîne de Newick spécifiée
	 * 
	 * @param newickNode une chaîne de Newick correspondant à un noeud, c'est à dire que la chaîne doit commencer par une parenthèse "("
	 * 		  et se terminer par une parenthèse ")".
	 */
	private static void fromNewickToTreeIter(Node root, String newickNode) {
		String temp = "";
		
		/* On boucle sur la chaîne newickNode et on ajoute un nouveau noeud comme enfant à root à chaque nouveau noeud lu comme
		 * enfant dans la chaîne */
		for (int i = 1; i < newickNode.length(); i++) {
			switch(newickNode.substring(i, i + 1)) {
				
			/* Si le caractètre lu est "(", cela signifie que c'est un noeud qui a des enfants qui vient d'être lu dans la chaîne.*/
				case "(":
					
					/* On ajoute donc un nouveau noeud comme enfant, et on ajoute les enfants de ce nouvel enfant en effectuant un appel récursif.*/
					Node child = new Node();
					root.addChild(child);
				
					/* J'envoie la chaîne qui constitue mon enfant, par exemple "(PHCD1_fjwnefw,jfnkerjfejr)" */
					fromNewickToTreeIter(child, newickNode.substring(i,  i + positionParFermante(newickNode.substring(i, newickNode.length())) + 1));
					
					/* Je reçois la position de la parenthèse fermante de mon enfant.
					 * La variable i va être incrémentée par la boucle for englobante et on passera au prochain enfant. */
					i += positionParFermante(newickNode.substring(i, newickNode.length()));
				
				case ",":
					/* Si une protéine a été lue avant la virgule, on ajoute cette protéine comme enfant de root*/
					if (temp != "") {
						root.addChild(new Node(temp));
					temp = "";
					}
					/* Sinon cela signifie qu'une parenthèse ")" précède la virgule, on avance donc à au prochain caractère.
					 * On ne fait rient puisque ")" signifie que l'on a vu un enfant qui a déjà été ajouté précedemment*/
					continue;
				 
				case ")":
					/* Ce cas correspond au dernier enfant qui rencontre la parenthèse fermante.*/
					if (temp != "")
						root.addChild(new Node(temp));
					return;
					
				default:
						/* Extraction de la protéine actuellement lue.*/
					temp = temp.concat(newickNode.substring(i, i + 1));
			}
		}
		
		/* Dans le cas où nous avons quitté la boucle for <=> nous avons atteint la fin du noeud et notre fonction n'a pas retournée */
		throw new IllegalArgumentException("Le noeud de Newick n'est pas au bon format");
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	


/// Transformation BinTree --> Newick //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Cette méthode permet d'obtenir un arbre binaire enraciné dans un format de chaîne de Newick.
	 * 
	 * @return Une chaîne de caractère en format Newick valide
	 */
	public String toNewick() {
		return toNewick(this.getRoot()).concat(";");
	}
	
	/**
	 * Cette méthode permet d'obtenir un arbre binaire enraciné dans un format de chaîne de Newick avec les distances.
	 * 
	 * @return Une chaîne de caractère en format Newick valide
	 */
	public String toNewickDist() {
		return toNewickDist(this.getRoot()).concat(";");
	}
	
	/**
	 * Cette méthode permet de transformer un arbre binaire enraciné en arbre au format Newick valide à l'aide d'appels récursifs.
	 * @param root
	 * 		  un noeud à partir duquel on souhaite construire une chaîne de Newick
	 * @param newickTree
	 * 		  une chaîne de caractère en format Newick qui est la chaîne correspondante à l'arbre engendré par root.
	 * 
	 * @return une chaîne de caractère en format Newick qui est la chaîne correspondante à l'arbre engendré par root.
	 */
	private static String toNewick(Node root) {
		if (root == null) 
			return "";
			
		String newickTree;
		if (root.getLeftChild() != null && root.getRightChild() != null) {
			newickTree = toNewick(root.getLeftChild()).concat(",").concat(toNewick(root.getRightChild()));
		}
		else if (root.getLeftChild() != null && root.getRightChild() == null) {
			newickTree = toNewick(root.getLeftChild());
		}
		else if (root.getLeftChild() == null && root.getRightChild() != null) {
			newickTree = toNewick(root.getRightChild());
		}
		else {
			newickTree = "";
		}
		
		/* Si c'est un noeud feuille, alors on retourne la protéine contenue dans la feuille */
		if (root.isLeave())
			return root.getProt();
		else
			return "(".concat(newickTree).concat(")");
	}
		
	/**
	 * Cette méthode permet de transformer un arbre binaire enraciné en arbre au format Newick valide avec les distances à l'aide d'appels récursifs.
	 * @param root
	 * 		  un noeud à partir duquel on souhaite construire une chaîne de Newick
	 * @param newickTree
	 * 		  une chaîne de caractère en format Newick qui est la chaîne correspondante à l'arbre engendré par root.
	 * 
	 * @return une chaîne de caractère en format Newick qui est la chaîne correspondante à l'arbre engendré par root.
	 */
	private static String toNewickDist(Node root) {
		if (root == null) 
			return "";
			
		String newickTree;
		if (root.getLeftChild() != null && root.getRightChild() != null) {
			newickTree = toNewickDist(root.getLeftChild()).concat(",").concat(toNewickDist(root.getRightChild()));
		}
		else if (root.getLeftChild() != null && root.getRightChild() == null) {
			newickTree = toNewickDist(root.getLeftChild());
		}
		else if (root.getLeftChild() == null && root.getRightChild() != null) {
			newickTree = toNewickDist(root.getRightChild());
		}
		else {
			newickTree = "";
		}
		
		/* Si c'est un noeud feuille, alors on retourne la protéine contenue dans la feuille */
		if (root.isLeave())
			return root.getProt().concat(":" + Double.toString(Math.round(root.getDist() * 10000.0) / 10000.0));
		else
			return "(".concat(newickTree).concat(")").concat(":" + Double.toString(Math.round(root.getDist() * 10000.0) / 10000.0));
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		


/// Question 3: Bipartitions et calcul de la distance RF /////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Compter le nombre de feuilles que contient un arbre binaire enraciné.
	 * 
	 * @return	le nombre de feuilles dans l'arbre binaire enraciné.	
	 */
	public int countLeaves() {
		if (this.root == null)
			return 0;
		else
			return this.root.countLeaves();
	}
	
	
	/**
	 * Mettre à jour l'ensemble de bipartitions non triviales d'un arbre enraciné.
	 * 
	 * @return Un ensemble de bipartitions correspondant aux protéines contenues aux feuilles. Possiblement vide.
	 * 
	 * @see #bipartitions
	 * @see #partitionner(Node)
	 */
	private HashSet<HashSet<HashSet<String>>> partitionnerArbre() {
		this.bipartitions = new HashSet<HashSet<HashSet<String>>>();
		if (this.countLeaves() > 3)
			this.partitionner(this.root);
		return this.bipartitions;
	}
	
	/**
	 * Suite récursive de la méthode partitionnerArbre() afin d'obtenir les bipartitions non-triviales à l'aide d'un parcours post-order.
	 * 
	 * @param root
	 * 		  un noeud racine à partir duquel l'on souhaite bipartition un arbre.
	 * 
	 * @return Un ensemble de bipartitions correspondant aux protéines contenues aux feuilles. Possiblement vide.
	 * 
	 * @see #transformBipartition(BinTree, HashSet)
	 * @see #countLeaves()
	 */
	private HashSet<String> partitionner(Node root) {
		
		/* Une partition est un ensemble de string {"PCDHA1_Humain", "PCDHA1_Rat"} forme par exemple une partition.
		 * Par exemple, {"PCDHA1_Humain", "PCDHA1_Rat"} est une partition de 
		 * la bipartition "PCDHA1_Humain","PCDHA1_Rat" | "PCDHA1_Bonobo", "PCDHA1_Souris", "OR2J3_Humain" */
		HashSet<String> partition = new HashSet<String>();
		
		/* Si la racine est vide, on retourne la partition vide {}*/
		if (root == null)
			return partition;
		
		/* Appels récursifs sur les partitions existantes aux enfants gauches et droit. En fait, on va effectuer un parcours post-order
		 * dans le but de trouver les bipartitions correspondantes à cet arbre.*/
		HashSet<String> partitionGauche = partitionner(root.getLeftChild());
		HashSet<String> partitionDroite = partitionner(root.getRightChild());
		
		/* Si la racine est une feuille, on retourne une partition contenant la protéine contenue dans ce noeud*/
		if (root.isLeave()) 
			partition.add(root.getProt());
		
		/* Sinon on fait l'union des partitions gauches et droites en supprimant les doublons*/
		else {
			partition.addAll(partitionGauche);
			partition.addAll(partitionDroite);
			
			/* On recherche les bipartitions non-triviales, la partition doit donc avoir une taille d'au moins 2 pour constituer une bipartition potentielle.
			 * et ne doit pas dépasser n - 2 feuilles (pour que l'autre partie de la bipartition soit de taille d'au moins 2)*/
			if (partition.size() >= 2 && partition.size() <= this.countLeaves() - 2)
				/* On peut donc ajouter la partition en faisant appel à transformBipartition
				 * qui va chercher les feuilles correspondant au reste de la partition et on forme donc une bipartition qui peut être ajouté
				 * dans l'ensemble de bipartitions de l'arbre.*/
				this.bipartitions.add(transformBipartition(this, partition));
		}
		return partition;
	}
	
	
	/**
	 * À partir d'une partition fournie en paramètre, et d'un arbre, obtenir la bipartition non-triviale correspondante.
	 *
	 * @param tree
	 * 		  un arbre binaire pour lequel on souhaite les valeurs contenues aux feuilles
	 * 
	 * @param partition
	 * 		  un ensemble qui contient 2 à n - 2 feuilles.
	 * 		 
	 * @return une bipartition, qui correspond à un ensemble de partition, chaque partition étant de de taille au moins 2.			
	 */
	private static HashSet<HashSet<String>> transformBipartition(BinTree tree, HashSet<String> partition) {
		/* On obtient toutes les protéines contenues aux feuilles d'un arbre que l'on met dans un ensemble reste*/
		HashSet<String> reste = tree.getLeavesProt();
		
		/* On retire de cet ensemble reste les feuilles existantes dans la partition.*/
		reste.removeAll(partition);
		
		/* On instancie donc une bipartition, qui est donc un ensemble d'ensemble de protéines.*/
		HashSet<HashSet<String>> bipartition = new HashSet<HashSet<String>>();
		bipartition.add(partition);
		bipartition.add(reste);
		return bipartition;
	}
	
	
	/**
	 * Calculer la distance topologique entre deux arbres binaires enracinés.
	 * 
	 * @param t2
	 * 		  un arbre binaire enraciné par rapport à lequel on souhaite calculer une distance topologique.
	 * 
	 * @return La distance topologique entre <code>this</code> et <code>t2</code>.
	 * 
	 * @see #getBipartitions()	 
	 */
	public double distanceTopo(BinTree t2) {
		HashSet<HashSet<HashSet<String>>> bipartitions = new HashSet<HashSet<HashSet<String>>>();
		HashSet<HashSet<HashSet<String>>> bipartT1 = this.getBipartitions();
		HashSet<HashSet<HashSet<String>>> bipartT2 = t2.getBipartitions();
		
		/* On fusionne l'ensemble de bipartitions de l'arbre T1 et de l'arbre T2 en n'autorisant pas les doublons*/
		bipartitions.addAll(bipartT1);
		bipartitions.addAll(bipartT2);
		
		/* S'il n'y pas de différence de taille, la valeur de la différence entre les bipartitions est de 0. 
		 * Sinon elle sera de 1, etc.*/
		return (double) 2 * ((double) bipartitions.size() - (double) bipartT1.size());
	}
	
	
	/**
	 * Calculer RF entre deux arbres binaires enracinés.
	 * 
	 * @param t2
	 * 		    un arbre binaire enraciné par rapport à lequel on souhaite calculer RF.
	 * 
	 * @return RF entre <code>this</code> et <code>t2</code>
	 * 
	 * @see #distanceTopo(BinTree)
	 */
	public double rf(BinTree t2) {
		double rf = ((double) this.distanceTopo(t2)) / ((double) (2 * (this.countLeaves() - 3)));
		return rf;
	}
	

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Assigne une des séquences d'acides aminés aux feuilles d'un arbre
	 * 
	 * @param proteins
	 * 		  une <code>HashMap</code> qui à une protéine associe sa séquence d'acides aminés.
	 * 
	 * @see #getLeavesNode()
	 */
	public void setSeqToLeaves(HashMap<String, String> proteins) {
		ArrayList<Node> leaves = this.getLeavesNode();
		for (int j = 0; j < leaves.size(); j++ )
				leaves.get(j).addSeq(proteins.get(leaves.get(j).getProt()));
	}
	
/// Question III : Algorithme mid-point //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/* Après avoir calculé tous les chemins de la racine vers les feuilles en pre-order, je dois recomposer les chemins.
	 * Un chemin se présentera par exemple sous la forme R 1 2 3 feuille, R 1 4 feuille. 1 est l'ancètre commun à ces deux chemins, donc
	 * ils forment un chemin entre deux feuilles. Je vais donc fusionner les deux listes en gardant le dernier élément en commun qui constitue l'élément racine
	 * de mon chemin et en inversant la seconde liste. Par exemple: R 1 6 8 14 feuille
	 * 																R 1 6 9 feuille
	 * Ce qui nous donne en fusionnant: 6 8 14 feuille
	 * 									6 9 feuille
	 * J'inverse la première liste, je ne garde qu'une copie de 6, ce qui donne le chemin : feuille 14 8 6 9 feuille
	 * 
	 * Ainsi je vérifie pour chaque chemin s'il existe des racines communes avec tous les autres chemins, et si oui, dans ce cas transformer le chemin
	 * en effectuant l'opération décrite plus haut.*/
	
	public ArrayList<ArrayList<Node>> getPaths() {
		ArrayList<ArrayList<Node>> Paths = new ArrayList<ArrayList<Node>>();
		ArrayList<Node> currentPath = new ArrayList<Node>();
		
		/* J'obtiens tous les chemins de la racine de l'arbre aux feuille en parcours pre-order*/
		this.getPaths(this.root, currentPath, Paths);
		
		ArrayList<ArrayList<Node>> newPaths = new ArrayList<ArrayList<Node>>();
		
		for (int i = 0; i < Paths.size(); i ++) {
			currentPath = Paths.get(i);

			/* Pour chaque chemin, le comparer avec le reste de tous les autres chemins */
			for (int j = i + 1; j < Paths.size(); j++) {
				int k;
				ArrayList<Node> newCurrent = new ArrayList<Node>();
				for (k = 0; k < currentPath.size(); k++) {
					/* Si racine commun break*/
					if (currentPath.get(k) != Paths.get(j).get(k)) {
						break;
					}
				}
				/* Inverser la première liste. Garder l'élément racine commun le plus proche pour le chemin*/
				for (int l = currentPath.size() - 1; l > k - 2; l--) {
					newCurrent.add(currentPath.get(l));
				}
				/* Ajouter la seconde liste*/
				for (int l = k; l < Paths.get(j).size(); l++) {
					newCurrent.add(Paths.get(j).get(l));
				}
				newPaths.add(newCurrent);
			}
		}
		/* Retourner enfin l'ensemble des chemins des feuilles aux autres feuilles sous forme d'une liste de liste de noeuds */
		return newPaths;
	}
	
	/**
	 * Parcours pre-order afin d'obtenir tous les chemins de la racine vers les feuilles.
	 * De cette façon, j'obtiens tous les chemins de la racine de mon arbre binaire vers les feuilles.
	 * 
	 * @param root
	 * 		  Noeud à partir duquel on souhaite calculer les chemins
	 * @param currentPath
	 * 		  un chemin actuellement calculé
	 * @param Paths
	 * 		  liste générale des chemins d'un arbre
	 * 
	 * @see #getPaths(Node, ArrayList, ArrayList)
	 */
	@SuppressWarnings("unchecked")
	private void getPaths(Node root, ArrayList<Node> currentPath, ArrayList<ArrayList<Node>> Paths) {
		/* Afin d'éviter les conflits d'instances */
		ArrayList<Node> newPath = (ArrayList<Node>) currentPath.clone();
		if (root == null)
			return;
		
		newPath.add(root);
		
		/* Si on atteint une feuille, je peux ajouter ce chemin dans la liste générale des chemins de mon arbre*/
		if (root.isLeave()) {
			Paths.add(newPath);
		}
		else {
			getPaths(root.getLeftChild(), newPath, Paths);
			getPaths(root.getRightChild(), newPath, Paths);
		}
	}
	
	/**
	 * Calcule la distance d'un noeud sur un chemin
	 * 
	 * @param path
	 * 		  le chemin sur lequel se trouv ele noeud
	 * 
	 * @param node
	 * 		  le noeud que l'on veut atteindre
	 * @return
	 * 
	 * @see #distNode(ArrayList, Node)
	 */
	public double distNode(ArrayList<Node> path, Node node) {
			double distance = 0;
			Node current = null;
			int i = 0;
			
			/* On peut incrémenter la variable de distance de la valeur des noeuds visités
			 * tant que l'on arrive pas au noeud recherché */
			while ((current = path.get(i)) != node) {
				if (this.getParent(current) !=  path.get(i + 1)) {
					/* Si nous arrivons à un moment où le parent n'est plus next, alors on break. On ne doit pas comptabiliser la distance de ce noeud
					 * par exemple: next    puis     current
					 * 			   /   \			/		\
					 * 		    current                      next
					 *
					 * Cela correspond au moment où nous atteignons la racine du chemin. */
					break;
				}
				distance += current.getDist();
				i++;
			}
			
			/* Si on est sorti de la boucle et que l'on a trouvé le noeud, on peut retourner.*/
			if (current == node) {
				return distance;
			}
			
			/* C'est le moment où notre chemin redescend de la racine du chemin. On swappe current et next:
					 * par exemple:   current			 next
					 * 			     /		 \		    /	 \
					 * 		                 next          current
					 *
			 *  Dans ce cas, on comptabilise
			 * d'abord la distance du noeud visité, et on vérifie ensuite si nous avons trouvé notre noeud*/
			do {
				current = path.get(i + 1);
				if (this.getRoot() != this.getParent(current)) {
					distance += current.getDist();
				}
				i++;
			} while (current != node);
			
			
			return distance;
		}
		
	/**
	 * Calculer la distance jusqu'au dernier noeud du chemin;
	 * 
	 * @param path
	 * @return la distance d'un chemin
	 * 
	 * @see #distNode(ArrayList, Node)
	 */
	public double distPath(ArrayList<Node> path) {
		return this.distNode(path, path.get(path.size() - 1));
	}
		
	public Node[] bornesMidPoint(ArrayList<Node> path, double mid) {	
		Node[] bornes = new Node[2];
		double distance = 0;
		Node current = null;
		Node next = null;
		int i = 0;
			
		while (distance < mid) {
			current = path.get(i);
			next = path.get(i + 1);
			if (this.getParent(current) !=  path.get(i + 1)) {
				break;
			}
			distance += current.getDist();
			i++;
		}
			
		if (distance >= mid) {
			bornes[0] = current;
			bornes[1] = next;
			return bornes;
		}
			
		do {
			current = path.get(i + 1);
			next = path.get(i);
			if (this.getRoot() != this.getParent(current)) {
				distance += current.getDist();
			}
			i++;
		} while (distance < mid);
			
		bornes[0] = current;
		bornes[1] = next;
		return bornes;
	}
	
	/**
	 * Trouver le chemin le plus long parmi une liste chemins dans un arbre
	 * @param Paths
	 * 		  une liste de chemins pour un arbre
	 * @return le chemin le plus long d'un arbre
	 * 
	 * @see #distPath(ArrayList)
	 */
	public ArrayList<Node> maxPath(ArrayList<ArrayList<Node>> Paths) {
		ArrayList<Node> path = null;
		double current;
		double max = Double.MIN_VALUE;
		for (int i = 0; i < Paths.size(); i++) {
			current = this.distPath(Paths.get(i));
			if (current > max) {
				max = current;
				path = Paths.get(i);
			}
		}
		return path;
	}
	
	/**
	 * Algorithme midPoint qui enracine un arbre binaire non-enraciné.
	 * 
	 * @see #getPaths()
	 * @see #maxPath(ArrayList)
	 * @see #distPath(ArrayList)
	 * @see #distNode(ArrayList, Node)
	 * @see #bornesMidPoint(ArrayList, double)
	 */
	public void midPoint() {
		/* On obtient tous les chemins de l'arbre binaire non enraciné d'une feuille à une autre avec l'appel à this.getPaths()
		 * Et on obtient le chemin maximum parmi tous ces chemins avec l'appel à la méthode maxPath(ArrayList)
		 * On représente un chemin par une suite de noeuds.*/
		ArrayList<Node> path = this.maxPath(this.getPaths());
		
		/* On obtientla longueur de ce chemin maximal, et on prend calcule le milieu. */
		double mid = (double) this.distPath(path) / 2;
		
		/* On fait ensuite appel à la fonction bornesMidPoint qui nous permet de trouver le noeud enfant et le noeud
		 * parent de l'arête sur laquelle est située le mid-point.*/
		Node[] bornes = this.bornesMidPoint(path, mid);
		/* La fonction nous retourne un tableau de noeuds qui contient en première position le noeud enfant de l'arête sur laquelle est situé le mid-point
		 * et le noeud parent en deuxieme position */
		Node current = bornes[0]; // current contient le noeud enfant
		Node next = bornes[1]; // next contient e noeud parent
			
		/* On conserve la distance du noeud parent. */
		double oldDist = next.getDist();
		
		double curDist;
		double nextDist;
			
		/* Si le noeud parent de l'arête est le noeud racine */
		if (next == this.root) {

			/* Calcul de la nouvelle distance pour le noeud enfant*/
			curDist = mid - this.distNode(path, current);
				
			/* Changement du noeud parent. Next va être changer en sibling de current.*/
			if (next.getLeftChild() == current) {
				next = next.getRightChild();
			}
			else if (next.getRightChild() == current) {
				next = next.getLeftChild();
			}
			/* Calcul de la nouvelle distance pour le sibling de current*/
			nextDist = current.getDist() - curDist;
		}
		else if (this.distNode(path, next) >= mid) {
			curDist = mid - this.distNode(path, current);
			nextDist = this.distNode(path, next) - mid;
		}
		else {
			curDist = this.distNode(path, current) - mid;
			nextDist = mid - this.distNode(path, next);
		}
			
		/* Affectation des distances pour les noeuds enfants et parents*/
		current.addDist(curDist);
		next.addDist(nextDist);
		
		/* On crée une nouvelle racine */
		Node root = new Node();
		
		/* Cette distance ne représente rien. On met -1 pour cela*/
		root.addDist(-1);
		
		/* On affecte le noeud enfant à gauche, et le noeud parent à droite.*/
		root.addLeftChild(current);
		root.addRightChild(next);
			
		/* Si nous sommes situés sur l'arête centrale, alors il suffit juste de changer la racine de l'arbre
		 * Nous avons effectués toutes les modifications nécessaires pour enraciner l'arbre.*/
		if (this.getParent(next) == this.root && this.getParent(current) == this.root) {
			this.root = root;
			return;
		}
			
		/* Sinon on itère jusqu'à remonter à la racine de l'arbre.
		 * On va comme effectuer des rotations. Tous les noeuds enfants de current n'ont pas besoin d'être modifiés.
		 * Ils seront situés à gauche de l'arbre.
		 * En revanche, les noeuds ancêtres de current doit maintenant devenir enfant de la nouvelle racine.
		 * Si current ---- next est une branche gauche/droite, (et donc on a next parent de current), alors l'enfant droit/gauche
		 * de next va devenir l'enfant gauche de next. Puis on va chercher le parent de next et le mettre comme enfant droit.
		 * et on avance à l'enfant droit de next (qui est la parent de next encore dans l'arbre) pour effectuer la même opération de rotation */
		while(true) {
			if (next.getLeftChild() == current)
				next.addLeftChild(next.getRightChild());
				
			/* Arriver à la racine, on ajoute le sous-arbre sibling et on peut retourner le nouvel arbre.*/
			if (next == this.root) {
				if (current == next.getLeftChild())
					current.addRightChild(next.getRightChild());
				else
					current.addRightChild(next.getLeftChild());
				break;
			}
				
			next.addRightChild(this.getParent(next));
			/* On avance le noeud current pour passer au noeud suivant*/
			current = next;
			next = next.getRightChild();
			double temp1 = next.getDist();
			next.addDist(oldDist);
			oldDist = temp1;
		}
		this.root = root;
		return;		
	}

/// Fin question III /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
 