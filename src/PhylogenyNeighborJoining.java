import java.io.*;
import java.util.*;

/**
 * 
 * Faire fonctionner le programme (exemple)
 * 
 * javac tp4.java
 * java tp4 /Users/Paul/Desktop/TP4/arbres.nw /Users/Paul/Desktop/TP4/proteines.fa /Users/Paul/Desktop/TP4/BLOSUM62.txt
 * 
 * 
 * Représentation d'arbres phylogénétiques et d'alignement:
 * 	<ol>
 *		<li>Élaborer une structure de donnée permettant de représenter un arbre: {@link BinTree#BinTree()}. Votre structure
 *			de donnée doit permettre d'effectuer une parcours en profondeur post-ordre: {@link BinTree#postOrder()} {@link Node#postOrder()}.
 *          Il faut pouvoir stocker de l'information aux noeuds: {@link Node}.</li>
 *		<li>Convertir une chaîne newick en une instance de votre structure d'arbre: {@link #construireArbres(ArrayList, HashMap)}.</li>
 *		<li>Être capable de calculer la distance RF entre deux instances de votre structure d'arbre: {@link BinTree#rf(BinTree)}}.</li>
 *		<li>Filtre les gaps des séquences: {@link #removeGaps(HashMap)}. Assigner les séquences aux feuilles correspondantes : 
 *			{@link #construireArbres(ArrayList, HashMap)} {@link BinTree#setSeqToLeaves(HashMap)}.</li>
 *	</ol>
 * Détermination de l'arbre NJ:
 * 	<ol>
 *		<li>Calculer la matrice de distance entre les séquences: {@link #matriceDistance(BinTree, double[][], HashMap)}</li>
 *		<li>Implémenter l'algorithme <i>Neighbor Joining</i>: {@link #neighborJoining(double[][], HashMap)}</li>
 *		<li>Calculer la distance RF entre l'arbre NJ et chacun des arbres entrés. Retourner l'arbre candidat ayant la plus petite distance RF avec l'arbre
 *			NJ: {@link #main(String[])}</li>
 *	</ol>
 * Enracinement de l'arbre NJ:
 * 	<ol>
 *		<li>Enracinement par <i>mid-point</i> pour un arbre non-enraciné: {@link BinTree#midPoint()}.</li>
 *	</ol>
 *
 *@author Paul Chaffanet - CHAP23049307
 */
public class PhylogenyNeighborJoining {
	
/// Traitement des fichiers passés en arguments ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Méthode qui permet de vérifier qu'un fichier a une extension ".nw".
	 * 
	 * @param file
	 * 		  un chemin vers un fichier.
	 * 
	 * @return <code>true</code> si le fichier a une extension ".nw".
	 * 		   <code>false</code> sinon.
	 */
	public static boolean isValidNewick(String file) {
		/* On extrait les 3 derniers caractères de la chaîne de caractère file et on vérifie s'ils sont égaux à
		 * la chaîne ".nw" */
		if (file.substring(file.length() - 3, file.length()).equals(".nw"))
			return true;
		else
			return false;
	}
	
	/**
	 * Méthode qui permet de vérifier qu'un fichier a une extension ".fa".
	 * 
	 * @param file
	 * 		  un chemin vers un fichier.
	 * @return <code>true</code> si le fichier a une extension ".fa".
	 * 		   <code>false</code> sinon.
	 */
	public static boolean isValidFa(String file) {
		/* On extrait les 3 derniers caractères de la chaîne de caractère file et on vérifie s'ils sont égaux à
		 * la chaîne ".fa" */
		if (file.substring(file.length() - 3, file.length()).equals(".fa"))
			return true;
		else
			return false;
	}
	
	/**
	 * Méthode qui permet de vérifier qu'un fichier a une extension ".txt".
	 * 
	 * @param file
	 * 		  un chemin vers un fichier.
	 * @return <code>true</code> si le fichier a une extension ".txt".
	 * 		   <code>false</code> sinon.
	 */
	public static boolean isValidTxt(String file) {
		/* On extrait les 4 derniers caractères de la chaîne de caractère file et on vérifie s'ils sont égaux à
		 * la chaîne ".txt" */
		if (file.substring(file.length() - 4, file.length()).equals(".txt"))
			return true;
		else
			return false;
	}
	
	/**
	 * Méthode qui permet de vérifier qu'une ligne contient une séquence d'acides aminés dans un format valide.
	 * 
	 * @param line
	 * 		  une chaîne de caractères à analyser.
	 * 
	 * @return <code>true</code> si la séquence protéinique est une séquence au format valie.
	 * 		   <code>false</code> sinon.
	 */
	public static boolean isValidSeq(HashMap<String, Integer> tableIndex, String line) {
		for (int i = 0; i < line.length(); i++) {
			/* On vérifie ici que chaque caractère lu est soit un caractère de la matrice de mutations, soit un gap "-".*/
			if (!(tableIndex.keySet().contains(line.substring(i, i + 1)) || line.substring(i, i + 1).equals("-"))) {
					/* Si la séquence contient un autre caractère que ceux spécifiés dans la matrice de mutation, alors la séquence d'acides aminés n'est pas valide.*/
					return false;
			}
		}
		return true;
	}
	
	/**
	 * <p>Cette méthode permet de vérifier que le nombre d'arguments entrés lors de l'exécution du programme soit exactement de trois:
	 * 	<ol>
  	 *		<li>Le premier argument passé en paramètre doit être le fichier "arbres.newick" (ou un fichier "*.newick")</li>
  	 *		<li>Le deuxième argument passé en paramètre doit être le fichier "proteines.fa" (ou un fichier "*.fa")</li>
  	 *		<li>Le troisième argument passé en paramètre doit être le fichier "BLOSUM62.txt" (ou un fichier "*.txt")</li>
	 *	</ol>
	 * </p>
	 * 
	 * @param args
	 * 		  Un tableau contenant les arguments entrés pour l'exécution du programme.
	 * 
	 * @return <code>true</code> si les arguments sont au bon format pour l'exécution du programme.
	 * 		   <code>false</code> sinon.
	 */
	public static boolean areValidArgs(String[] args) {
		/* On vérifie que le nombre d'arguments est de 3*/
		/* On vérifie la validité des extensions fichiers.*/
 		if (args.length == 3 && isValidNewick(args[0]) && isValidFa(args[1]) && isValidTxt(args[2]))
			return true;
		else
			return false;
	}
		
	/**
	 * <p>Cette méthode permet d'obtenir un <code>BufferedReader</code> d'un fichier afin de pouvoir lire ligne après ligne un fichier.</p>
	 * 
	 * @param file
	 * 		  un chemin vers un fichier à lire.
	 * 
	 * @return Retourne un <code>BufferedReader</code> qui nous permet de pouvoir lire les lignes d'un fichier.	   
	 */
	public static BufferedReader getBufReader(String file) {
		BufferedReader buffer = null;
	
		/* Instanciation d'un BufferedReader afin de pouvoir lire les lignes du fichier spécifié.*/
		try {
			buffer = new BufferedReader(new FileReader(file));
		}
		
		/* Si le fichier spécifié n'existe pas, si le fichier est un répertoire, ou pour tout autre raison pour
		 * laquelle un fichier ne peut être ouvert pour être lu. */
		catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Le fichier ne peut être lu.");
		}
		return buffer;
	}
	
	/**
	 * Enlever les gaps parmi une liste de séquences d'acides aminés.
	 * 
	 * @param proteins
	 * 		  une map qui à une protéine associe sa séquence d'acides aminés.
	 * 
	 * @return <code>HashMap<String, String></code> qui à une protéine associe sa séquence d'acides aminés filtrée des gaps.
	 */
	public static HashMap<String, String> removeGaps(HashMap<String, String> proteins) {
	    
		ArrayList<Integer> positionsGap = new ArrayList<Integer>();
	    String seq;
	    
	    /* Dans un premier passage de boucle, je vais noter toutes les positions des gaps dans toutes les séquences
	     * aminés.*/
	    for (String key : proteins.keySet()) {
	    	seq = proteins.get(key);
	    	for(int i = 0; i < seq.length(); i++) {
	    		if (seq.substring(i, i + 1).equals("-")) {
	    			if (!positionsGap.contains(i)) {
	    				positionsGap.add(i);
	    			}
	    		}
	    	}
	    }
	    
	    /* Dans un deuxième passage, je vais cette fois retirer les gaps aux positions relevées précedemment.*/
	    for (String key : proteins.keySet()) {
	    	seq = proteins.get(key);
	    	int j = 0;
	    	int longueur = seq.length();
	    	
	    	/* Je parcours toute ma sequence et je retire les gaps. Comme je retire des caractères, les positions changent au fur
	    	 * et à mesure. J'utilise donc une variable j qui me permet de calculer par rapport à la position initiale la position
	    	 * réelle du gap que je dois supprimer */
	    	for(int i = 0; i < longueur; i++) {
	    		if (positionsGap.contains(i)) {
	    			seq = seq.substring(0, i + j).concat(seq.substring(i + 1 + j));
	    			j--;
	    		}
	    	}
	    	/* J'insère alors la nouvelle séquence dépourvue qui est maintenant filtrée des gaps.*/
	    	proteins.put(key, seq);
	    }
	    return proteins;
	}
	
	/**
	 * Cette méthode lit les lignes d'un fichier "*.nw", supprime les espaces blancs dans chaque ligne,
	 * et les ajoute dans une <code>ArrayList</code>.
	 * Ainsi toute ligne non-vide est considérée comment étant un nouvel arbre à ajouter dans la <code>ArrayList</code>. Cette liste contient
	 * donc potentiellement des arbres dont le format Newick est invalide.
	 * 
	 * @param file
	 * 		  un chemin vers un fichier au format "*.nw" à lire
	 * 
	 * @return Retourne une <code>ArrayList</code> de toutes les lignes non-vides d'un fichier "*.nw".
	 */
	public static ArrayList<String> getNewick(String file) {
		
		/* Si le fichier n'est pas un fichier ".nw", alors on ne peut pas utiliser cette méthode pour obtenir une liste d'arbres
		 * de Newick. On quitte le programme qui ne peut donc s'exécuter correctement.*/
		if (!isValidNewick(file)) {
			System.out.println("Le fichier n'est pas au format \".nw\". Le programme ne peut pas s'exécuter normalement.");
			System.exit(1);
		}
		
		/* Instanciation d'un BufferedReader afin de pouvoir lire les lignes de mon fichier.*/
		BufferedReader buffer = getBufReader(file);
		ArrayList<String> arbres = null;
		
		/* Début de la boucle de lecture ligne par ligne du fichier. On remplace tous les espaces blancs de la ligne par une chaîne vide
		 * puis on ajoute la nouvelle chaîne dans la liste des arbres. Cette nouvelle chaîne est donc considéré comme un arbre. À cette étape là,
		 * nous n'avons pas encore vérifier la validité du format Newick. Cette étape de vérification du format de la chaîne comme arbre
		 * de Newick s'effectuera au moment de la transformation de la chaîne en arbre binaire enraciné.*/
		try {
			String line;
			arbres = new ArrayList<String>();
			while((line = buffer.readLine()) != null) {
				if (!line.equals("")) 
					arbres.add(line.replaceAll("\\s", ""));
			}	
		}
		
		/* Si une erreur entrée/sortie se produit lors de la lecture ligne par ligne du fichier, on affiche un message pour signaler
		 * l'erreur et on quitte le programme. */
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("Un problème est survenu lors de la lecture du fichier " + file + ".");
			System.exit(1);
		}
		finally {
			if (buffer != null) {
				try {
					buffer.close();
				}
				
				/* Si une erreur entrée/sortie se produit lors de la fermeture du BufferedReader, on affiche un message pour signaler
				 * l'erreur et on quitte le programme. */
				catch (IOException e) {
					e.printStackTrace();
					System.out.println("Une erreur est survenue lors de la fermeture du buffer.");
					System.exit(1);
				}
			}
		}
		
		/* Une fois la lecture du fichier terminée, on retourne une ArrayList des arbres lus dans le fichier
		 * L'étape de transformation de ces chaînes de caractères en arbres binaire s'effectuera plus tard par l'appel d'une
		 * autre méthode.
		 * Les arbres lus et insérés dans la liste ne sont pas garantis d'avoir un format valide Newick. */
		return arbres;
	}
	
	/**
	 * Cette méthode lit les lignes d'un fichier "*.fa". À chaque ligne lue, on supprime tous les espaces blancs.
	 * La méthode retourne une <code>HashMap</code> contenant toutes les protéines lues dans le fichier et leurs séquences
	 * génétique associées.
	 * 
	 * @param file
	 * 		  un chemin vers un fichier au format "*.fa" à lire.
	 * 
	 * @return Retourne une <code>HashMap</code> contenant toutes les protéines lues dans le fichier et leurs séquences
	 * génétique associées.
	 */
	public static HashMap<String, String> getProteins(HashMap<String, Integer> tableIndex, String file) {
		
		/* Si le fichier n'est pas un fichier ".fa", alors on ne peut pas utiliser cette méthode pour obtenir
		 * la liste des protéines et leurs séquences associées. On quitte le programme qui ne peut donc s'exécuter correctement.*/
		if (!isValidFa(file)) {
			System.out.println("Le fichier n'est pas au format \".fa\". Le programme ne peut pas s'exécuter normalement.");
			System.exit(1);
		}
		
		/* Instanciation d'un BufferedReader afin de pouvoir lire les lignes de mon fichier. */
		BufferedReader buffer = getBufReader(file);
		
		HashMap<String,String> proteins = null;
		try {
			
			String line;
			String prot = "";
			proteins = new HashMap<String,String>();
			
			/* Cette boucle permet de lire ligne après ligne mon fichier */
			while((line = buffer.readLine()) != null) {
				
				/* À chaque ligne lue, on supprimer= tous les espaces existants dans la chaîne.*/
				line = line.replaceAll("\\s", "");
				
				/* Si la ligne lue commence par le caractère ">", cela signifie que cette ligne contient un nom de protéine.*/
				if (line.substring(0, 1).equals(">")) {
					
					/* On extrait donc le nom de la protéine de la ligne, et on insère cette protéine comme clé
					 * dans ma HashMap et qui contient pour le moment une séquence vide "".*/
					prot = line.substring(1, line.length());
					proteins.put(prot, "");
				}
				
				/* Sinon si une protéine a été lue précédemment dans le fichier, et que la ligne ne commence pas par ">", alors
				 * on considère que cette ligne lue est un bout de la séquence génétique de la protéine précédemment lue.
				 * On insère donc dans la HashMap la valeur précédemment contenue et l'on concatène la nouvelle ligne.
				 */
				else if (!prot.equals("") && isValidSeq(tableIndex, line)) {
					proteins.put(prot, proteins.get(prot).concat(line));
				}
				
				/* Si aucune protéine n'a été lue précédemment, alors le fichier lu n'est pas au bon format. On ne peut pas
				 * procéder à l'extraction d'une séquence génétique étant donné qu'aucune protéine n'a été signalée avant
				 * dans le fichier. */
				else {
					System.out.println("Le fichier n'est pas au format \".fa\". Le programme ne peut pas s'exécuter normalement.");
					System.exit(1);
				}
			}	
		}
		
		/* Si une erreur entrée/sortie se produit lors de la lecture ligne par ligne du fichier, on affiche un message pour signaler
		 * l'erreur et on quitte le programme. */
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("Un problème est survenu lors de la lecture du fichier " + file + ".");
			System.exit(1);
		}
		finally {
			if (buffer != null) {
				try {
					buffer.close();
				}

				/* Si une erreur entrée/sortie se produit lors de la fermeture du BufferedReader, on affiche un message pour signaler
				 * l'erreur et on quitte le programme. */
				catch (IOException e) {
					e.printStackTrace();
					System.out.println("Une erreur est survenue lors de la fermeture du buffer.");
				}
			}
		}
		
		/* Avant de retourner la HashMap des protéines avec leur séquence, je filtre les gaps contenues dans les séquences*/
		proteins = removeGaps(proteins);
		return proteins;
	}
	
	/**
	 * Cette méthode lit les lignes d'un fichier "*.txt". À chaque ligne lue, on supprime tous les espaces blancs.
	 * La méthode retourne une <code>HashMap</code> contenant toutes les protéines lues dans le fichier et leurs séquences
	 * d'acides aminés associées.
	 * 
	 * @param file
	 * 		  un chemin vers un fichier au format "*.fa" à lire.
	 * 
	 * @return Retourne une matrice de nombres à double précision <code>double[][]</code> contenant toutes les valeurs des mutations
	 *  	   de la matrice BLOSUM lue dans le fichier
	 */
	public static Object[] getBlosum(String file) {	
		
		/* Si le fichier n'est pas un fichier ".txt", alors on ne peut pas utiliser cette méthode pour obtenir une matrice
		 * de mutation à partir du fichier. On quitte le programme qui ne peut donc s'exécuter correctement.*/
		if (!isValidTxt(file)) {
			System.out.println("Le fichier n'est pas au format \".txt\". Le programme ne peut pas s'exécuter normalement.");
			System.exit(1);
		}
		
		/* On instancie une HashMap qui à un acide aminé associe un index dans la matrice des mutations.
		 * Cela correspond donc à la position (ligne = colonne) de l'acide aminé dans la matrice des mutations. Ainsi on pourra
		 * accéder au coût de mutation d'un acide aminé très facilement lorsque l'on aura besoin de calculer la distance entre 
		 * les différentes séquences d'acides aminés. */
		HashMap<String, Integer> tableIndex = new HashMap<String, Integer>();

		/* On crée initialise une matrice de nombres à double précision. Cette matrice contiendra tous les coûts associés aux mutations
		 * des acides aminés. La tableIndex nous permettra d'accéder facilement à la bonne position dans cette matrice.*/
		double[][] matriceBlosum = null;
		
		/* Instanciation d'un BufferedReader afin de pouvoir lire les lignes de mon fichier.*/
		BufferedReader buffer = getBufReader(file);
		
		/* On initialise une variable lineNumber à 0. Cette variable va nous permettre comme son nom l'entend de connaître
		 * le numéro de ligne sur lequel nous sommes situés dans le fichier.*/
		int lineNumber = 0;
		
		/* La variable end va nous indiquer que la matrice a fini d'être lue. À partir de la dernière ligne *, end sera mis à true
		 * et toutes les lignes y compris la ligne * qui suivront seront ignorées lors de la lecture du fichier. */
		boolean end = false;
		try {					
			String line;
			while((line = buffer.readLine()) != null && ++lineNumber > 0) {
				
				/* Traitement de la première ligne d'un fichier ".txt" contenant une matrice au format BLOSUM */
				if(lineNumber == 1) {
					/* Sur la première ligne du fichier, on découpe la ligne séparé par des espaces dans un tableau. Ce tableau
					 * est susceptible de contenir des chaînes "" que nous traiterons un peu plus loin.*/
					String[] charac = line.split("\\s");
					
					int j = 0;
					for(int i = 0; i < charac.length; i++) {
						
						/* On parcourt tous le tableau qui contient les acides aminés situés sur la première ligne de la matrice BLOSUM.
						 * On ignore les chaînes "" et "*" susceptibles de se retrouvés dans le tableau charac à cause de la fonction split
						 * en passant au prochain élément du tableau charac (encore une fois, ce n'est pas très élégant mais efficace...).*/
						
						if (!(charac[i].equals("") || charac[i].equals("*"))) {
							/* On vérifie que l'acide aminé a une longueur 1 et est tel que A <= acide aminé <=Z. Si c'est le cas, on insère l'acide
							 * aminé et la colonne associé à cet acide aminé dans la matrice BLOSUM dans table des index de la matrice. 
							 * Sinon on quitte le programme en signalant l'erreur.*/
							if (charac[i].length() == 1 && charac[i].charAt(0) >= 65 && charac[i].charAt(0) <= 90) {
								tableIndex.put(charac[i], j++);
							}
							else {
								System.out.println("La matrice BLOSUM n'est pas au bon format. Les acides aminées doit respecter l'expression régulière \"[A-Z]{1}\"");
								System.exit(1);
							}
						}
					}
					
					/* Après la phase de prétraitement de la première ligne du fichier BLOSUM, on initialise la matrice à la taille de la tableIndex.*/
					matriceBlosum = new double[tableIndex.size()][tableIndex.size()];
				}
				else if (!end) {
					/* On entre ici dans le cas où la matrice le numéro de ligne est la ligne numéro 2.
					 * On traite chaque ligne pour affecter les cellules de ma matrice BLOSUM */
					boolean debut = true;
					String[] charac = line.split("\\s");
			
					int j = 0;
					for (int i = 0; i < charac.length; i++) {
						
						/* On ignore les chaînes vides susceptibles de se retrouver dans le tableau charac à cause de la méthode split.*/
						if (charac[i].equals("")) {
							continue;
						}
						/* Si le premier caractère lu de la dernière ligne de la matrice est "*", alors on ignore la ligne.
						 * On met la variable à end à true et on ignore toute la suite du fichier.*/
						else if (charac[i].equals("*") && debut && lineNumber == tableIndex.size() + 2) {
							end = true;
							break;
						}
						
						/*  On vérifie que le premier caractère de ligne est un acide aminé qui existe déjà dans la tableIndex qui contient
						 *  tous les acides aminés lus sur la première ligne. On vérifie également que son index est cohérent par rapport
						 *  à l'index relevé sur la première ligne.*/
						else if (tableIndex.containsKey(charac[i]) 
						&& tableIndex.get(charac[i]) == lineNumber - 2 
						&& debut) {
							debut = false;
							continue;
						}
						
						/* Tout caractère de la ligne doit être dans un format de nombre valide. Sinon une exception sera levée,
						 * que l'on traitera en signalant à l'utilisateur que sa matrice n'est pas dans un format conforme.*/
						else if (!debut && j < tableIndex.size()) {
							try {
								matriceBlosum[lineNumber - 2][++j - 1] = Double.parseDouble(charac[i]);
							}
							catch (NumberFormatException e) {
								System.out.println("La matrice BLOSUM n'est pas au bon format. Le programme ne peut s'exécuter normalement.");
								System.exit(1);
							}
						}
						else if (j >= tableIndex.size()) {
							continue;
						}
						else {
							System.out.println("La matrice BLOSUM n'est pas au bon format. Le programme ne peut s'exécuter normalement.");
							System.exit(1);
						}
					}
				}
				
				/* On ignore le suite du fichier. La matrice BLOSUM a déjà été vérifiée comment étant au bon format.*/
				else if (end) {
					continue;
				}
			}	
		}
		
		/* Si une erreur entrée/sortie se produit lors de la lecture ligne par ligne du fichier, on affiche un message pour signaler
		 * l'erreur et on quitte le programme. */
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("Un problème est survenu lors de la lecture du fichier " + file + ".");
			System.exit(1);
		}
		finally {
			if (buffer != null) {
				try {
					buffer.close();
				}
				/* Si une erreur entrée/sortie se produit lors de la fermeture du BufferedReader, on affiche un message pour signaler
				 * l'erreur et on quitte le programme. */
				catch (IOException e) {
					e.printStackTrace();
					System.out.println("Une erreur est survenue lors de la fermeture du buffer.");
				}
			}
		}
		
		/* On retourne enfin un tableau d'objet contenant en première position la table d'index des acides aminés
		 * pour la matrice de mutations et en deuxième position la matrice de mutations contenant les coûts de mutations. */
		return new Object[]{tableIndex, matriceBlosum};	
	}
	
/// Constructions des arbres enracinés  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Construire les arbres binaires enracinés correspondant aux chaînes de newick. Associer les séquences d'acides aminés aux feuilles.
	 * 
	 * @param arbresNewick
	 * 		  une liste de chaînes en format newick.
	 * 
	 * @param proteins
	 * 		  une HashMap qui associe une protéine à sa séquence d'acides aminés.
	 * 
	 * @return une liste de <code>BinTree</code> qui contient les arbres binaires enracinés avec les protéines et leurs séquences d'acides aminés
	 * 		   associée aux feuilles.
	 * 
	 * @see BinTree#fromNewickToTree(String)
	 * @see BinTree#setSeqToLeaves(HashMap)
	 */
	public static ArrayList<BinTree> construireArbres(ArrayList<String> arbresNewick, HashMap<String, String> proteins) {
		ArrayList<BinTree> arbresBinEnracines = new ArrayList<BinTree>();
		
		for (int i = 0; i < arbresNewick.size(); i++) {
			BinTree tree = BinTree.fromNewickToTree(arbresNewick.get(i));
			tree.setSeqToLeaves(proteins);
			arbresBinEnracines.add(tree);
		}
		return arbresBinEnracines;
	}
	
	/**
	 * Permet de calculer la distance entre deux séquences étant donné une matrice de coûts pour les mutations.
	 * 
	 * @param matriceMutations
	 * 		  la matrice qui contient les coûts de mutations des acides aminés pour les séquences.
	 * @param tableIndex
	 * 		  une HashMap qui à un caractère d'acide aminé (par exemple "A", "L") associe un index dans la matrice de mutations.
	 * @param seq1
	 * @param seq2
	 * 		  les deux séquences dont on veut calculer la distance
	 * 
	 * @return la distance entre <code>seq1</code> et <code>seq2</code>
	 */
	public static double distance(double[][] matriceMutations, HashMap<String, Integer> tableIndex, String seq1, String seq2) {
		double p, qi, qj;
		p = qi = qj = 0;
		
		for (int i = 0; i < seq1.length(); i++) {
			p += matriceMutations[tableIndex.get(seq1.substring(i, i + 1))][tableIndex.get(seq2.substring(i, i + 1))];
			qi += matriceMutations[tableIndex.get(seq1.substring(i, i + 1))][tableIndex.get(seq1.substring(i, i + 1))];
			qj += matriceMutations[tableIndex.get(seq2.substring(i, i + 1))][tableIndex.get(seq2.substring(i, i + 1))];
		}
		return (1 - (double) p / (double) Math.max(qi, qj));
	}
	
	/**
	 * Calculer la matrice des distances par rapport à une liste de séquences. On va chercher pour un arbre ses noeuds feuilles
	 * et on va calculer la distance entre les séquences situées au feuilles afin de pouvoir construire une matrice des distances
	 * pour la liste de séquences.
	 * On crée également une <code>HashMap<code> tableNoeud qui va garder l'index d'un noeud dans la matrice des distrance.
	 * Ainsi, on pourra facilement accéder à un noeud à partir de la matrice et vice-versa.
	 * 
	 * @param tree
	 * 		  un arbre binaire dont on souhaite obtenir les séquences situées aux feuilles
	 * @param matriceMutations
	 * 		  la matrice qui contient les coûts de mutations des acides aminés pour les séquences.
	 * 	
	 * @param tableIndex
	 * 		  une <code>HashMap<code> qui à un caractère d'acide aminé (par exemple "A", "L") associe un index dans la matrice de mutations.
	 * 
	 * @return Un tableau d'objets de 2 éléments. En première position, il y aura une <code>HashMap</code> qui sera la table d'index des noeuds pour la matrice
	 * des distances. En deuxième position se trouve la matrice des distances pour les séquences.
	 * 
	 * @see BinTree#getLeavesNode();
	 * @see #distance(double[][], HashMap, String, String)
	 */
	public static Object[] matriceDistance(BinTree tree, double[][] matriceMutations, HashMap<String, Integer> tableIndex) {
		ArrayList<Node> leaves = tree.getLeavesNode();
		
		double[][] matriceDistance = new double[leaves.size()][leaves.size()];
		HashMap<Integer, Node> tableNoeud = new HashMap<Integer, Node>(leaves.size());
		
		for (int i = 0; i < matriceDistance.length; i++) {
			for (int j = 0; j < matriceDistance[i].length; j++)
				matriceDistance[i][j] = distance(matriceMutations, tableIndex, leaves.get(i).getSeq(), leaves.get(j).getSeq());
			tableNoeud.put(i, leaves.get(i));
		}
		
		return new Object[]{tableNoeud, matriceDistance};
	}
	
/// Détermination de l'arbre NJ: question 2 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Calculer de la valeur ri
	 * 
	 * @param matriceDistance
	 * 		  la matrice de distance
	 * @param index
	 * 		  la valeur i qui ne doit pas être sommée
	 * @return La valeur ri
	 */
	public static double ri (double[][] matriceDistance, int index) {
		
		double ri = 0;
		
		for(int k = 0; k < matriceDistance.length; k++)
			if (k != index)
				ri += matriceDistance[index][k];
		
		ri = ri * ((double) 1 / (matriceDistance.length - 2));
		return ri;
	}
	
	/**
	 * Calcul de la matrice NJ associées à la matrice des distances.
	 * 
	 * @param matriceDistance
	 * 		  la matrice des distances à partir de la quelle on veut construire la matrice NJ
	 * @param tableNoeuds
	 * 		  index des noeuds dans la matrice des distances
	 * 
	 * @return la matrice NJ
	 * 
	 * @see #ri(double[][], int)
	 */
	public static double[][] NJmatrix(double[][] matriceDistance, HashMap<Integer, Node> tableNoeuds) {
		
		double[][] NJmatrix = new double[matriceDistance.length][matriceDistance.length];

		for (int i = 0; i < matriceDistance.length; i++)
			for (int j = 0; j < matriceDistance.length; j++)
				if (i != j)
					NJmatrix[i][j] = matriceDistance[i][j] - ri(matriceDistance, i) - ri(matriceDistance, j);
		
		return NJmatrix;
	}

	/**
	 * Trouver la valeur minimale dans la matrice NJ
	 * 
	 * @param matriceNJ
	 * @return un tableau <code>[i,j]</code> qui représente la position de l'élément minimal
	 */
	public static int[] findMin(double[][] matriceNJ) {
		int[] index = new int[2];
		double min = Double.MAX_VALUE;
		for (int i = 0; i < matriceNJ.length; i++) {
			for (int j = 0; j < matriceNJ[i].length; j++) {
				if (matriceNJ[i][j] < min && i != j) {
					min = matriceNJ[i][j];
					index = new int[] {i, j};
				}
			}
		}
		return index;
	}
	
	/**
	 * Calcul de la nouvelle ligne et colonne du noeud K à insérer dans la matrice des distances
	 * 
	 * @param matriceDistance
	 * @param index
	 * 		  index feuilles i et j
	 * @return
	 */
	public static double[] vecteurK(double[][] matriceDistance, int[] index) {
		double[] vecteurK = new double[matriceDistance.length + 1];
		for (int m = 0; m < matriceDistance.length; m++) {
			vecteurK[m] = ((double) 1/2)*(matriceDistance[index[0]][m] + matriceDistance[index[1]][m] - matriceDistance[index[0]][index[1]]);
		}
		return vecteurK;
	}
	
	/**
	 * Ajouter d'une ligne/colonne dans la matrice des distances
	 * 
	 * @param matriceDistance
	 * @param vecteurK
	 * @return la nouvelle matrice pourvue du vecteurK.
	 * 
	 */
	public static double[][] ajouteVecteurK(double[][] matriceDistance, double[] vecteurK) {
		double[][] matrice = new double[matriceDistance.length + 1][matriceDistance.length + 1];
		for (int i = 0; i < matrice.length - 1; i++) {
			for (int j = 0; j < matrice[i].length - 1; j++) {
				matrice[i][j] = matriceDistance[i][j];
			}
		}
		matrice[matriceDistance.length] = vecteurK;
		for (int i = 0; i < matrice.length; i++) {
			matrice[i][matrice.length - 1] = vecteurK[i];
		}
		return matrice;
	}
	
	/**
	 * Supprimer une ligne/colonne dans la matrice des distances.
	 * 
	 * @param matriceDistance
	 * @param i
	 * 		  la ligne/colonne que l'on veut supprimer
	 * @return la nouvelle matrice des distances.
	 */
	public static double[][] delete(double[][] matriceDistance, int i) {
		double[][] matrice = new double[matriceDistance.length - 1][matriceDistance.length - 1];
		
		for (int k = 0; k < matrice.length; k++) {
			for (int j = 0; j < matrice[0].length; j++) {
				if (k >= i && j >= i)
					matrice[k][j] = matriceDistance[k + 1][j + 1];
				else if (k >= i && j < i)
					matrice[k][j] = matriceDistance[k + 1][j];
				else if (k < i && j >= i)
					matrice[k][j] = matriceDistance[k][j + 1];
				else
					matrice[k][j] = matriceDistance[k][j];
			}
		}
		
		return matrice;
	}
	
	/**
	 * Algorithme NJ
	 * 
	 * @param matriceDistance
	 * @param tableNoeud
	 * @return
	 * 
	 * @see #NJmatrix(double[][], HashMap)
	 * @see #findMin(double[][])
	 * @see #vecteurK(double[][], int[])
	 * @see #ajouteVecteurK(double[][], double[])
	 * @see #delete(double[][], int)
	 * @see #matriceDistance(BinTree, double[][], HashMap)
	 * 
	 */
	public static BinTree neighborJoining(double[][] matriceDistance, HashMap<Integer, Node> tableNoeud) {
		/* L contient désormais la table d'index des noeuds pour la matrice des distances*/
		HashMap<Integer, Node> L = tableNoeud;
		double[][] matriceNJ;
		
		/* Le vecteurK correspond à la ligne/colonne dans la matrice des distances d'un nouveau noeud k à insérer.*/
		double[] vecteurK;
		Node i;
		Node j;
		Node k;
		int[] index;
		
		/* Itération sur L, où L est le nombre de lignes/colonnes restantes dans la matrice des distances*/
		while (L.size() > 2) {
			/* Calcul de la matrice NJ*/
			matriceNJ = NJmatrix(matriceDistance, L);
			
			/* Trouver la valeur minimale [i,j] dans la matrice NJ*/
			index = findMin(matriceNJ);
			
			/* Ajouter i et j comme enfant d'un nouveau noeud k*/
			k = new Node();
			i = L.get(index[0]);
			j = L.get(index[1]);
			k.addChild(i);
			k.addChild(j);
			
			/* Mettre à jour les distances des arêtes [i,k] et [j, k] */
			i.addDist(((double) 1/2)*(matriceDistance[index[0]][index[1]] + ri(matriceDistance, index[0]) - ri(matriceDistance, index[1])));
			j.addDist(matriceDistance[index[0]][index[1]] - i.getDist());
			
			/* Calculer la nouvelle ligne/colonne pour le noeud K dans la matrice des distances*/
			vecteurK = vecteurK(matriceDistance, index);
			
			ArrayList<Integer> keys = new ArrayList<Integer>();
			
			/* On retire de la table des index la position i*/
			L.remove(index[0]);
			
			/* On énumère tous les index de L*/
			for (Integer key : L.keySet()) {
				keys.add(key);
			}
			
			
			for (Integer key: keys) {
				Node temp = L.get(key);
				/* Si un des index de L est supérieur à la ligne i, alors son nouvel index sera décrémenté de 1.
				 * Sinon on ne change pas son index dans la matrice des distances*/
				if (key > index[0]) {
					L.remove(key);
					L.put(key - 1, temp);
				}
			}
			
			/* On répète l'opération pour j, mais en sachant cette fois que i a été retiré et que les index ont été mis à jour. */
			keys = new ArrayList<Integer>();
			for (Integer key : L.keySet()) {
				keys.add(key);
			}
			/* Si i était situé après j, la position de j n'a pas changé */
			if (index[0] > index[1]) {
				L.remove(index[1]);
				for (Integer key: keys) {
					/* Puis maj des index pour j comme avec i plus haut*/
					Node temp = L.get(key);
					if (key > index[1]) {
						L.remove(key);
						L.put(key - 1, temp);
					}
				}
			}
			else {
				/* Sinon on doit décrémenter de 1*/
				L.remove(index[1] - 1);
				for (Integer key: keys) {
					Node temp = L.get(key);
					/* Puis maj des index pour j comme avec i plus haut*/
					if (key > index[1] - 1) {
						L.remove(key);
						L.put(key - 1, temp);
					}
				}
			}
			
			/* Ajouter l'index du nouveau noeud k dans la matrice des distances*/
			L.put(L.size(), k);
			
			/* Ajouter la nouvelle ligne/colonne du noeud k*/
			matriceDistance = ajouteVecteurK(matriceDistance, vecteurK);
			
			/* Supprimer la ligne i de la matrice*/
			matriceDistance = delete(matriceDistance, index[0]);
			/* Si i plus grand que j, la position de j n'a pas changé*/
			if (index[0] > index[1]) {
				matriceDistance = delete(matriceDistance, index[1]);
			}
			/* Sinon il est nécessaire de décrementer j de 1 pour retrouver la bonne ligne/colonne à supprimer*/
			else {
				matriceDistance = delete(matriceDistance, index[1] - 1);
			}
		}
		
		/* A la fin de l'algorithme, on retourne le nouvel arbre non-enraciné.*/
		/* Ici, la structure de donnée est faite pour les arbres enracinés. Dans ce cas, nous allons mettre la distance de root à -1. */
		BinTree tree = new BinTree(new Node());
		tree.getRoot().addChild(L.get(0));
		tree.getRoot().addChild(L.get(1));
		
		/* Les enfants gauche et droit de root auront la même valeur de distance, mais il ne faudra compter cette arête qu'une fois lors de l'enracinement avec
		 * mid-point.*/
		tree.getRoot().getLeftChild().addDist(matriceDistance[0][1]);
		tree.getRoot().getRightChild().addDist(matriceDistance[0][1]);
		tree.getRoot().addDist(-1);
		return tree;
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		if (!areValidArgs(args)) {
			System.out.println("Les arguments entrés ne sont pas au bon format.\n\n"
					+ "Le premier argument passé en paramètre doit être le fichier \"arbres.nw\" (ou un fichier \"*.nw\")\n"
					+ "Le deuxième argument passé en paramètre doit être le fichier \"proteines.fa\" (ou un fichier \"*.fa\")\n"
					+ "Le troisième argument passé en paramètre doit être le fichier \"BLOSUM62.txt\" (ou un fichier \"*.txt\")\n");
			System.exit(1);
		}
		
		/* Traitement du fichier "arbres.newick"*/
		ArrayList<String> arbresNewick = getNewick(args[0]);
	    

		/* Traitement du fichier "BLOSUM62.txt"*/
		
		Object[] result = getBlosum(args[2]);
		/* HashMap qui va contenir l'index des acides aminés dans la matrice de mutations qui correspond à ceux entrés dans la matrice BLOSUM.*/
	    HashMap<String, Integer> tableIndex = (HashMap<String, Integer>) result[0];
	    /* La matrice de mutations qui contient les coûts inscrits dans la matrice BLOSUM*/
	    double[][] matriceMutation = (double[][]) result[1];
	    
	    /* Traitement du fichier "proteins.fa". On obtient les protéines du ficher et leur séquence associée*/
	    HashMap<String, String> proteins = getProteins(tableIndex, args[1]);
	    
	    /* Construction des arbres binaires enracinés.
	     * Les arbres ont aux feuilles les protéines associées à leur séquence d'acides aminés filtrée.*/
	    ArrayList<BinTree> arbresBinEnracines = construireArbres(arbresNewick, proteins);
	     
		
	    /* Appel à la fonction matriceDistance. On obtient donc la matrice des distances entre les séquences et la table d'index pour cette
	     * matrice*/
		Object[] objets = matriceDistance(arbresBinEnracines.get(0), matriceMutation, tableIndex);
		/*  tableNoeuds contient la position des noeuds dans la matrice des distances*/
		HashMap<Integer, Node> tableNoeuds = (HashMap<Integer, Node>) objets[0];
		double[][] matriceDistance = (double[][]) objets[1];
		
		/* Appel à neighborJoining pour créer un arbre au plus proche d'un arbre additif*/
		BinTree tree = neighborJoining(matriceDistance, tableNoeuds);
		
		/* Calculer et retourner la distance RF entre l'arbre Neighbor et chacun des arbres entrés*/
		for (int i = 0; i < arbresBinEnracines.size(); i++) {
			System.out.println("Arbre: " + arbresBinEnracines.get(i).toNewick());
			System.out.println("Arbre NJ: " + tree.toNewick());
			System.out.println("Distance: " + tree.rf(arbresBinEnracines.get(i)));
			System.out.println();
		}
		
		/* Candidat ayant la plus petite distance RF avec l'arbres NJ*/
		double min = Double.MAX_VALUE;
		int index = -1;
		for (int i = 0; i < arbresBinEnracines.size(); i++) {
			if (tree.rf(arbresBinEnracines.get(i)) < min) {
				min = tree.rf(arbresBinEnracines.get(i));
				index = i;
			}
		}
		System.out.println("Arbre candidat ayant la plus petite distance RF: " + arbresBinEnracines.get(index).toNewick());
		System.out.println();
		
		/* Enracinement par mid-point de l'arbre NJ*/
		tree.midPoint();
		System.out.println("Arbre NJ enraciné par mid-point: " + tree.toNewickDist());
	}

}
