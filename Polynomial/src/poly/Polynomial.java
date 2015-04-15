package poly;

import java.io.*;
import java.util.StringTokenizer;

/**
 * This class implements a term of a polynomial.
 * 
 * @author runb-cs112
 *
 */
class Term {
	/**
	 * Coefficient of term.
	 */
	public float coeff;
	
	/**
	 * Degree of term.
	 */
	public int degree;
	
	/**
	 * Initializes an instance with given coefficient and degree.
	 * 
	 * @param coeff Coefficient
	 * @param degree Degree
	 */
	public Term(float coeff, int degree) {
		this.coeff = coeff;
		this.degree = degree;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		return other != null &&
		other instanceof Term &&
		coeff == ((Term)other).coeff &&
		degree == ((Term)other).degree;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (degree == 0) {
			return coeff + "";
		} else if (degree == 1) {
			return coeff + "x";
		} else {
			return coeff + "x^" + degree;
		}
	}
}

/**
 * This class implements a linked list node that contains a Term instance.
 * 
 * @author runb-cs112
 *
 */
class Node {
	
	/**
	 * Term instance. 
	 */
	Term term;
	
	/**
	 * Next node in linked list. 
	 */
	Node next;
	
	/**
	 * Initializes this node with a term with given coefficient and degree,
	 * pointing to the given next node.
	 * 
	 * @param coeff Coefficient of term
	 * @param degree Degree of term
	 * @param next Next node
	 */
	public Node(float coeff, int degree, Node next) {
		term = new Term(coeff, degree);
		this.next = next;
	}
}

/**
 * This class implements a polynomial.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Pointer to the front of the linked list that stores the polynomial. 
	 */ 
	Node poly;
	
	/** 
	 * Initializes this polynomial to empty, i.e. there are no terms.
	 *
	 */
	public Polynomial() {
		poly = null;
	}
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param br BufferedReader from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 */
	public Polynomial(BufferedReader br) throws IOException {
		String line;
		StringTokenizer tokenizer;
		float coeff;
		int degree;
		
		poly = null;
		
		while ((line = br.readLine()) != null) {
			tokenizer = new StringTokenizer(line);
			coeff = Float.parseFloat(tokenizer.nextToken());
			degree = Integer.parseInt(tokenizer.nextToken());
			poly = new Node(coeff, degree, poly);
		}
	}
	
	
	/**
	 * Returns the polynomial obtained by adding the given polynomial p
	 * to this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial to be added
	 * @return A new polynomial which is the sum of this polynomial and p.
	 */
	public Polynomial add(Polynomial p) {
		Node p1 = poly;
		Node p2 = p.poly;
		int m1 = 0;
		for (Node ptr = p1; ptr != null; ptr = ptr.next) {
			if (ptr.term.degree > m1) m1 = ptr.term.degree;
		}
		int m2 = 0;
		for (Node ptr = p2; ptr != null; ptr = ptr.next) {
			if (ptr.term.degree > m2) m2 = ptr.term.degree;
		}
		int max;
		if (m2 > m1) {
			p1 = p.poly;
			p2 = poly;
			max = m2;
		}
		else{
			max = m1;
			p1 = poly;
			p2 = p.poly;
		}
		Polynomial tot = new Polynomial();
		tot.poly = new Node(0,0, null);		
		Node ptot = tot.poly;
		for(int i = 0; i <= max; i++){
			Node pt1 = p1;
			while (pt1 != null) {
				if (pt1.term.degree == i) break;
				pt1 = pt1.next;
			}
			Node pt2 = p2;
			while (pt2 != null) {
				if(pt2.term.degree == i) break;
				pt2 = pt2.next;
			}
			if(pt1 == null && pt2 != null) {
				ptot.next = new Node(pt2.term.coeff, pt2.term.degree, null);
				ptot = ptot.next;
			}
			else if(pt2 == null && pt1 != null) {
				ptot.next = new Node(pt1.term.coeff, pt1.term.degree, null);
				ptot = ptot.next;
			}
			else if(pt1 != null && pt2 != null) {
				if(pt1.term.coeff + pt2.term.coeff != 0) {
					ptot.next = new Node(pt1.term.coeff + pt2.term.coeff, pt1.term.degree, null);
					ptot = ptot.next;
				}
			}
		}
		tot.poly = tot.poly.next;
		return tot;
	}
	
	/**
	 * Returns the polynomial obtained by multiplying the given polynomial p
	 * with this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial with which this polynomial is to be multiplied
	 * @return A new polynomial which is the product of this polynomial and p.
	 */
	public Polynomial multiply(Polynomial p) {
		Node p1 = poly;
		Node p2 = p.poly;
		if (p1 == null || p2 == null) return new Polynomial();
		Polynomial tot = new Polynomial();
		tot.poly = new Node(0,0, null);
		Node p3 = tot.poly;
		while (p1 != null) {
			while (p2 != null) {
				p3.next = new Node(p1.term.coeff * p2.term.coeff, p1.term.degree + p2.term.degree, null);
				p2 = p2.next;
				p3 = p3.next;
			}
			p2 = p.poly;
			p1 = p1.next;
		}
		p3 = tot.poly.next;
		Node p5 = p3;
		while (p3 != null) {
			while (p5.next != null) {
				if(p3.term.degree == p5.next.term.degree) {
					p3.term.coeff += p5.next.term.coeff;
					p5.next = p5.next.next;
				}
				else p5 = p5.next;
			}
			p3 = p3.next;
			p5 = p3;
		}
		Polynomial result = new Polynomial();
		result.poly = new Node(0,0,null);
		Node p6 = result.poly;
		tot.poly = tot.poly.next;
		System.out.println("First degree: " + tot.poly.term.degree);
		int max = 0;
		for (Node ptr = tot.poly; ptr != null; ptr = ptr.next) {
			if (ptr.term.degree > max) max = ptr.term.degree;
		}
		for (int i = 0; i <= max; i++) {
			p5 = tot.poly;
			while(p5 != null) {
				if (p5.term.degree == i) {
					p6.next = new Node(p5.term.coeff, p5.term.degree, null);
					p6 = p6.next;
				}
				p5 = p5.next;
			}
		}
		result.poly = result.poly.next;
		return result;
	}
	
	/**
	 * Evaluates this polynomial at the given value of x
	 * 
	 * @param x Value at which this polynomial is to be evaluated
	 * @return Value of this polynomial at x
	 */
	public float evaluate(float x) {
		Node ptr = poly;
		float co = 0;
		int deg = 0;
		float result = 0;
		while (ptr != null) {
			co = ptr.term.coeff;
			deg = ptr.term.degree;
			float ini = 1;
			while (deg != 0){ 
				ini *= x;
				deg--;
			}
			ptr = ptr.next;
			result += co * ini;
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String retval;
		
		if (poly == null) {
			return "0";
		} else {
			retval = poly.term.toString();
			for (Node current = poly.next ;
			current != null ;
			current = current.next) {
				retval = current.term.toString() + " + " + retval;
			}
			return retval;
		}
	}
}
