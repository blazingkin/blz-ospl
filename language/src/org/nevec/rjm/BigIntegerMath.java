package org.nevec.rjm ;

import java.math.BigInteger;
import java.util.Vector;


/** BigInteger special functions and Number theory.
* @since 2009-08-06
* @author Richard J. Mathar
*/
public class BigIntegerMath
{





        /** Evaluate binomial(n,k).
        * @param n The upper index 
        * @param k The lower index
        * @return The binomial coefficient
        */
        static public BigInteger binomial(final int n, final int k)
        {
                if ( k == 0 ) 
                        return(BigInteger.ONE) ;
                BigInteger bin = new BigInteger(""+n) ;
                BigInteger n2 = bin ;
                for(BigInteger i= new BigInteger(""+(k-1)) ; i.compareTo(BigInteger.ONE) >= 0 ; i = i.subtract(BigInteger.ONE) )
                        bin = bin.multiply(n2.subtract(i)) ;
                for(BigInteger i= new BigInteger(""+k) ; i.compareTo(BigInteger.ONE) == 1 ; i = i.subtract(BigInteger.ONE) )
                        bin = bin.divide(i) ;
                return ( bin) ;
        } /* binomial */

        /** Evaluate binomial(n,k).
        * @param n The upper index 
        * @param k The lower index
        * @return The binomial coefficient
        * @since 2008-10-15
        */
        static public BigInteger binomial(final BigInteger n, final BigInteger k)
        {
                /* binomial(n,0) =1 
                */
                if ( k.compareTo(BigInteger.ZERO) == 0 ) 
                        return(BigInteger.ONE) ;

                BigInteger bin = new BigInteger(""+n) ;

                /* the following version first calculates n(n-1)(n-2)..(n-k+1)
                * in the first loop, and divides this product through k(k-1)(k-2)....2
                * in the second loop. This is rather slow and replaced by a faster version
                * below
                * BigInteger n2 = bin ;
                * BigInteger i= k.subtract(BigInteger.ONE) ;
                * for( ; i.compareTo(BigInteger.ONE) >= 0 ; i = i.subtract(BigInteger.ONE) )
                *       bin = bin.multiply(n2.subtract(i)) ;
                * i= new BigInteger(""+k) ;
                * for( ; i.compareTo(BigInteger.ONE) == 1 ; i = i.subtract(BigInteger.ONE) )
                *       bin = bin.divide(i) ;
                */

                /* calculate n then n(n-1)/2 then n(n-1)(n-2)(2*3) etc up to n(n-1)..(n-k+1)/(2*3*..k)
                * This is roughly the best way to keep the individual intermediate products small
                * and in the integer domain. First replace C(n,k) by C(n,n-k) if n-k<k.
                */
                BigInteger truek = new BigInteger(k.toString()) ;
                if ( n.subtract(k).compareTo(k) < 0 )
                        truek = n.subtract(k) ;

                /* Calculate C(num,truek) where num=n and truek is the smaller of n-k and k.
                * Have already initialized bin=n=C(n,1) above. Start definining the factorial
                * in the denominator, named fden
                */
                BigInteger i = new BigInteger("2") ;
                BigInteger num = new BigInteger(n.toString()) ;
                /* a for-loop   (i=2;i<= truek;i++)
                */
                for( ; i.compareTo(truek) <= 0 ; i = i.add(BigInteger.ONE) )
                {
                        /* num = n-i+1 after this operation
                        */
                        num = num.subtract(BigInteger.ONE) ;
                        /* multiply by (n-i+1)/i
                        */
                        bin = (bin.multiply(num)).divide(i) ;
                }
                return ( bin) ;
        } /* binomial */

        /** Evaluate sigma_k(n).
        * @param n the main argument which defines the divisors
        * @param k the lower index, which defines the power
        * @return The sum of the k-th powers of the positive divisors
        */
        static public BigInteger sigmak(final BigInteger n, final int k)
        {
                return (new Ifactor(n.abs())).sigma(k).n ;
        } /* sigmak */

        /** Evaluate sigma(n).
        * @param n the argument for which divisors will be searched.
        * @return the sigma function. Sum of the positive divisors of the argument.
        * @since 2006-08-14
        * @author Richard J. Mathar
        */
        static public BigInteger sigma(int n)
        {
                return (new Ifactor(Math.abs(n))).sigma().n ;
        }

        /** Compute the list of positive divisors.
        * @param n The integer of which the divisors are to be found.
        * @return The sorted list of positive divisors.
        * @since 2010-08-27
        * @author Richard J. Mathar
        */
        static public Vector<BigInteger> divisors(final BigInteger n)
        {
                return (new Ifactor(n.abs())).divisors() ;
        }

        /** Evaluate sigma(n).
        * @param n the argument for which divisors will be searched.
        * @return the sigma function. Sum of the divisors of the argument.
        * @since 2006-08-14
        * @author Richard J. Mathar
        */
        static public BigInteger sigma(final BigInteger n)
        {
                return (new Ifactor(n.abs())).sigma().n ;
        }

        /** Evaluate floor(sqrt(n)).
        * @param n The non-negative argument.
        * @return The integer square root. The square root rounded down.
        * @since 2010-08-27
        * @author Richard J. Mathar
        */
        static public int isqrt(final int n)
        {
                if ( n < 0 )
                        throw new ArithmeticException("Negative argument "+ n) ;
                final double resul= Math.sqrt((double)n) ;
                return (int)Math.round(resul) ;
        }

        /** Evaluate floor(sqrt(n)).
        * @param n The non-negative argument.
        *  Arguments less than zero throw an ArithmeticException.
        * @return The integer square root, the square root rounded down.
        * @since 2010-08-27
        * @author Richard J. Mathar
        */
        static public long isqrt(final long n)
        {
                if ( n < 0 )
                        throw new ArithmeticException("Negative argument "+ n) ;
                final double resul= Math.sqrt((double)n) ;
                return Math.round(resul) ;
        }

        /** Evaluate floor(sqrt(n)).
        * @param n The non-negative argument.
        *  Arguments less than zero throw an ArithmeticException.
        * @return The integer square root, the square root rounded down.
        * @since 2011-02-12
        * @author Richard J. Mathar
        */
        static public BigInteger isqrt(final BigInteger n)
        {
                if ( n.compareTo(BigInteger.ZERO) < 0 )
                        throw new ArithmeticException("Negative argument "+ n.toString()) ;
                /* Start with an estimate from a floating point reduction.
                */
                BigInteger x  ;
                final int bl = n.bitLength() ;
                if ( bl > 120)
                        x = n.shiftRight(bl/2-1) ;
                else
                {
                        final double resul= Math.sqrt(n.doubleValue()) ;
                        x = new BigInteger(""+Math.round(resul)) ;
                }

                final BigInteger two = new BigInteger("2") ;
                while ( true)
                {
                        /* check whether the result is accurate, x^2 =n
                        */
                        BigInteger x2 = x.pow(2) ;
                        BigInteger xplus2 = x.add(BigInteger.ONE).pow(2) ;
                        if ( x2.compareTo(n) <= 0 && xplus2.compareTo(n) > 0)
                                return x ;
                        xplus2 = xplus2.subtract(x.shiftLeft(2)) ;
                        if ( xplus2.compareTo(n) <= 0 && x2.compareTo(n) > 0)
                                return x.subtract(BigInteger.ONE) ;
                        /* Newton algorithm. This correction is on the
                        * low side caused by the integer divisions. So the value required
                        * may end up by one unit too large by the bare algorithm, and this
                        * is caught above by comparing x^2, (x+-1)^2 with n.
                        */
                        xplus2 = x2.subtract(n).divide(x).divide(two) ;
                        x = x.subtract(xplus2) ;
                }
        }

        /** Evaluate core(n).
        * Returns the smallest positive integer m such that n/m is a perfect square.
        * @param n The non-negative argument.
        * @return The square-free part of n.
        * @since 2011-02-12
        * @author Richard J. Mathar
        */
        static public BigInteger core(final BigInteger n)
        {
                if ( n.compareTo(BigInteger.ZERO) < 0 )
                        throw new ArithmeticException("Negative argument "+ n) ;
                final Ifactor i = new Ifactor(n) ;
                return i.core() ;
        }

        /** Minor of an integer matrix.
        * @param A The matrix.
        * @param r The row index of the row to be removed (0-based).
        *   An exception is thrown if this is outside the range 0 to the upper row index of A.
        * @param c The column index of the column to be removed (0-based).
        *   An exception is thrown if this is outside the range 0 to the upper column index of A.
        * @return The depleted matrix. This is not a deep copy but contains references to the original.
        * @since 2010-08-27
        * @author Richard J. Mathar
        */
        static public BigInteger[][] minor(final BigInteger[][] A, final int r, final int c) throws ArithmeticException
        {
                /* original row count */
                final int rL = A.length ;
                if ( rL == 0 )
                        throw new ArithmeticException("zero row count in matrix") ;
                if ( r < 0  || r >= rL)
                        throw new ArithmeticException("row number "+r + " out of range 0.." + (rL-1)) ;
                /* original column count */
                final int cL = A[0].length ;
                if ( cL == 0 )
                        throw new ArithmeticException("zero column count in matrix") ;
                if ( c < 0  || c >= cL)
                        throw new ArithmeticException("column number "+c + " out of range 0.." + (cL-1)) ;
                BigInteger M[][] = new BigInteger[rL-1][cL-1] ;
                int imrow =0 ;
                for (int row = 0 ; row < rL ; row++)
                {
                        if ( row != r)
                        {
                                int imcol = 0 ;
                                for(int col = 0 ; col < cL ;col++)
                                {
                                        if ( col != c )
                                        {
                                                M[imrow][imcol] = A[row][col] ;
                                                imcol ++ ;
                                        }
                                }
                                imrow++ ;
                        }
                }
                return M ;
        }


        /** Determinant of an integer square matrix.
        * @param A The square matrix.
        *  If column and row dimensions are unequal, an ArithmeticException is thrown.
        * @return The determinant.
        * @since 2010-08-27
        * @author Richard J. Mathar
        */
        static public BigInteger det(final BigInteger[][] A) throws ArithmeticException
        {
                BigInteger d = BigInteger.ZERO ;
                /* row size */
                final int rL = A.length ;
                if ( rL == 0 )
                        throw new ArithmeticException("zero row count in matrix") ;
                /* column size */
                final int cL = A[0].length ;
                if ( cL != rL )
                        throw new ArithmeticException("Non-square matrix dim "+rL + " by " + cL) ;

                /* Compute the low-order cases directly.
                */
                if ( rL == 1 )
                        return A[0][0] ;

                else if ( rL == 2)
                {
                        d = A[0][0].multiply(A[1][1]) ;
                        return d.subtract( A[0][1].multiply(A[1][0])) ;
                }
                else
                {
                        /* Work arbitrarily along the first column of the matrix */
                        for (int r = 0  ; r < rL  ; r++)
                        {
                                /* Do not consider minors that do no contribute anyway
                                */
                                if ( A[r][0].compareTo(BigInteger.ZERO) != 0 )
                                {
                                        final BigInteger M[][] = minor(A,r,0) ;
                                        final BigInteger m = A[r][0].multiply( det(M)) ;
                                        /* recursive call */
                                        if ( r % 2 == 0)
                                                d = d.add(m) ;
                                        else
                                                d = d.subtract(m) ;
                                }
                        }
                }
                return d;
        }

        /** Solve a linear system of equations.
        * @param A The square matrix.
        *  If it is not of full rank, an ArithmeticException is thrown.
        * @param rhs The right hand side. The length of this vector must match the matrix size;
        *  else an ArithmeticException is thrown.
        * @return The vector of x in A*x=rhs.
        * @since 2010-08-28
        * @author Richard J. Mathar
        */
        static public Rational[] solve(final BigInteger[][]A, final BigInteger[] rhs) throws ArithmeticException
        {

                final int rL = A.length ;
                if ( rL == 0 )
                        throw new ArithmeticException("zero row count in matrix") ;

                /* column size */
                final int cL = A[0].length ;
                if ( cL != rL )
                        throw new ArithmeticException("Non-square matrix dim "+rL + " by " + cL) ;
                if ( rhs.length != rL )
                        throw new ArithmeticException("Right hand side dim "+ rhs.length + " unequal matrix dim " + rL) ;

                        /* Gauss elimination 
                        */
                        Rational x[] = new Rational[rL] ;

                        /* copy of r.h.s ito a mutable Rationalright hand side
                        */
                        for(int c = 0 ; c < cL ; c++)
                                x[c] = new Rational(rhs[c]) ;

                        /* Create zeros downwards column c  by linear combination of row c and row r.
                        */
                        for(int c = 0 ; c < cL-1 ; c++)
                        {
                                /* zero on the diagonal? swap with a non-zero row, searched with index r */
                                if ( A[c][c].compareTo(BigInteger.ZERO) == 0)
                                {
                                        boolean swpd = false ;
                                        for(int r=c+1; r< rL ; r++)
                                        {
                                                if ( A[r][c].compareTo(BigInteger.ZERO) != 0)
                                                {
                                                        for(int cpr =c ; cpr < cL; cpr++)
                                                        {
                                                                BigInteger tmp = A[c][cpr] ;
                                                                A[c][cpr] = A[r][cpr] ;
                                                                A[r][cpr] = tmp ;
                                                        }
                                                        Rational tmp = x[c] ;
                                                        x[c] = x[r] ;
                                                        x[r] = tmp ;
                                                        swpd = true ;
                                                        break;
                                                }
                                        }
                                        /* not swapped with a non-zero row: determinant zero and no solution
                                        */
                                        if ( ! swpd)
                                                throw new ArithmeticException("Zero determinant of main matrix") ;
                                }
                                /* create zero at A[c+1..cL-1][c] */
                                for( int r=c+1; r < rL ; r++)
                                {
                                        /* skip the cpr=c which actually sets the zero: this element is not visited again
                                        */
                                        for(int cpr = c+1; cpr < cL; cpr++)
                                        {
                                                BigInteger tmp = A[c][c].multiply(A[r][cpr]) .subtract ( A[c][cpr].multiply(A[r][c])) ;
                                                A[r][cpr] = tmp ;
                                        }
                                        Rational tmp = x[r].multiply(A[c][c]) .subtract ( x[c].multiply(A[r][c])) ;
                                        x[r] = tmp ;
                                }
                        }
                        if ( A[cL-1][cL-1].compareTo(BigInteger.ZERO) == 0)
                                throw new ArithmeticException("Zero determinant of main matrix") ;
                        /* backward elimination */
                        for( int r = cL-1 ; r >= 0 ; r--)
                        {
                                x[r] = x[r].divide(A[r][r]) ;
                                for(int rpr = r-1 ; rpr >=0 ; rpr--)
                                        x[rpr] = x[rpr].subtract( x[r].multiply(A[rpr][r]) ) ;
                        }

                        return x ;
        }

        /** The lowest common multiple
        * @param a The first argument
        * @param b The second argument
        * @return lcm(|a|,|b|)
        * @since 2010-08-27
        * @author Richard J. Mathar
        */
        static public BigInteger lcm(final BigInteger a, final BigInteger b)
        {
                BigInteger g = a.gcd(b) ;
                return a.multiply(b).abs().divide(g) ;
        }


        /** Evaluate the value of an integer polynomial at some integer argument.
        * @param c Represents the coefficients c[0]+c[1]*x+c[2]*x^2+.. of the polynomial
        * @param x The abscissa point of the evaluation
        * @return The polynomial value.
        * @since 2010-08-27
        * @author Richard J. Mathar
        */
        static public BigInteger valueOf(final Vector<BigInteger>c, final BigInteger x)
        {
                if (c.size() == 0)
                        return BigInteger.ZERO ;
                BigInteger res = c.lastElement() ;
                for(int i= c.size()-2 ; i >=0 ; i--)
                        res = res.multiply(x).add( c.elementAt(i) ) ;
                return res ;
        }

        /** The central factorial number t(n,k) number at the indices provided.
        * @param n the first parameter, non-negative.
        * @param k the second index, non-negative.
        * @return t(n,k)
        * @since 2009-08-06
        * @author Richard J. Mathar
        * @see <a href="http://dx.doi.org/10.1080/01630568908816313">P. L. Butzer et al, Num. Funct. Anal. Opt. 10 (5)( 1989) 419-488</a>
        */
        static public Rational centrlFactNumt(int n,int k)
        {
                if ( k > n || k < 0 || ( k % 2 ) != (n % 2) )
                        return Rational.ZERO ;
                else if ( k == n)
                        return Rational.ONE ;
                else
                {
                        /* Proposition 6.2.6 */
                        Factorial f = new Factorial() ;
                        Rational jsum = new Rational(0,1) ;
                        int kprime = n-k ;
                        for ( int j =0 ; j <= kprime ; j++)
                        {
                                Rational nusum = new Rational(0,1) ;
                                for(int nu =0 ; nu <= j ; nu++)
                                {
                                        Rational t = new Rational(j-2*nu,2) ;
                                        t = t.pow(kprime+j) ;
                                        t = t.multiply( binomial(j,nu) ) ;
                                        if ( nu % 2 != 0 )
                                                nusum = nusum.subtract(t) ;
                                        else
                                                nusum = nusum.add(t) ;
                                }
                                nusum = nusum.divide( f.at(j) ).divide(n+j) ;
                                nusum = nusum.multiply( binomial(2*kprime,kprime-j) ) ;
                                if ( j % 2 != 0 )
                                        jsum = jsum.subtract(nusum) ;
                                else
                                        jsum = jsum.add(nusum) ;
                        }
                        return jsum.multiply(k).multiply( binomial(n+kprime,k) ) ;
                }
        } /* CentralFactNumt */

        /** The central factorial number T(n,k) number at the indices provided.
        * @param n the first parameter, non-negative.
        * @param k the second index, non-negative.
        * @return T(n,k)
        * @since 2009-08-06
        * @author Richard J. Mathar
        * @see <a href="http://dx.doi.org/10.1080/01630568908816313">P. L. Butzer et al, Num. Funct. Anal. Opt. 10 (5)( 1989) 419-488</a>
        */
        static public Rational centrlFactNumT(int n,int k)
        {
                if ( k > n || k < 0 || ( k % 2 ) != (n % 2) )
                        return Rational.ZERO ;
                else if ( k == n)
                        return Rational.ONE ;
                else
                {
                        /* Proposition 2.1 */
                        return centrlFactNumT(n-2,k-2).add( centrlFactNumT(n-2,k).multiply(new Rational(k*k,4)) ) ;
                }
        } /* CentralFactNumT */


} /* BigIntegerMath */
