package org.nevec.rjm ;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.security.ProviderException;


/** BigDecimal special functions.
* <a href="http://arxiv.org/abs/0908.3030">A Java Math.BigDecimal Implementation of Core Mathematical Functions</a>
* @since 2009-05-22
* @author Richard J. Mathar
* @see <a href="http://apfloat.org/">apfloat</a>
* @see <a href="http://dfp.sourceforge.net/">dfp</a>
* @see <a href="http://jscience.org/">JScience</a>
*/
public class BigDecimalMath
{

        /** The base of the natural logarithm in a predefined accuracy.
        * http://www.cs.arizona.edu/icon/oddsends/e.htm
        * The precision of the predefined constant is one less than
        * the string's length, taking into account the decimal dot.
        * static int E_PRECISION = E.length()-1 ;
        */
        static BigDecimal E = new BigDecimal("2.71828182845904523536028747135266249775724709369995957496696762772407663035354"+
"759457138217852516642742746639193200305992181741359662904357290033429526059563"+
"073813232862794349076323382988075319525101901157383418793070215408914993488416"+
"750924476146066808226480016847741185374234544243710753907774499206955170276183"+
"860626133138458300075204493382656029760673711320070932870912744374704723069697"+
"720931014169283681902551510865746377211125238978442505695369677078544996996794"+
"686445490598793163688923009879312773617821542499922957635148220826989519366803"+
"318252886939849646510582093923982948879332036250944311730123819706841614039701"+
"983767932068328237646480429531180232878250981945581530175671736133206981125099"+
"618188159304169035159888851934580727386673858942287922849989208680582574927961"+
"048419844436346324496848756023362482704197862320900216099023530436994184914631"+
"409343173814364054625315209618369088870701676839642437814059271456354906130310"+
"720851038375051011574770417189861068739696552126715468895703503540212340784981"+
"933432106817012100562788023519303322474501585390473041995777709350366041699732"+
"972508868769664035557071622684471625607988265178713419512466520103059212366771"+
"943252786753985589448969709640975459185695638023637016211204774272283648961342"+
"251644507818244235294863637214174023889344124796357437026375529444833799801612"+
"549227850925778256209262264832627793338656648162772516401910590049164499828931") ;

        /** Euler's constant Pi.
        * http://www.cs.arizona.edu/icon/oddsends/pi.htm
        */
        static BigDecimal PI = new BigDecimal("3.14159265358979323846264338327950288419716939937510582097494459230781640628620"+
"899862803482534211706798214808651328230664709384460955058223172535940812848111"+
"745028410270193852110555964462294895493038196442881097566593344612847564823378"+
"678316527120190914564856692346034861045432664821339360726024914127372458700660"+
"631558817488152092096282925409171536436789259036001133053054882046652138414695"+
"194151160943305727036575959195309218611738193261179310511854807446237996274956"+
"735188575272489122793818301194912983367336244065664308602139494639522473719070"+
"217986094370277053921717629317675238467481846766940513200056812714526356082778"+
"577134275778960917363717872146844090122495343014654958537105079227968925892354"+
"201995611212902196086403441815981362977477130996051870721134999999837297804995"+
"105973173281609631859502445945534690830264252230825334468503526193118817101000"+
"313783875288658753320838142061717766914730359825349042875546873115956286388235"+
"378759375195778185778053217122680661300192787661119590921642019893809525720106"+
"548586327886593615338182796823030195203530185296899577362259941389124972177528"+
"347913151557485724245415069595082953311686172785588907509838175463746493931925"+
"506040092770167113900984882401285836160356370766010471018194295559619894676783"+
"744944825537977472684710404753464620804668425906949129331367702898915210475216"+
"205696602405803815019351125338243003558764024749647326391419927260426992279678"+
"235478163600934172164121992458631503028618297455570674983850549458858692699569"+
"092721079750930295532116534498720275596023648066549911988183479775356636980742"+
"654252786255181841757467289097777279380008164706001614524919217321721477235014") ;

        /** Euler-Mascheroni constant lower-case gamma.
        * http://www.worldwideschool.org/library/books/sci/math/MiscellaneousMathematicalConstants/chap35.html
        */
        static BigDecimal GAMMA = new BigDecimal("0.577215664901532860606512090082402431"+
"0421593359399235988057672348848677267776646709369470632917467495146314472498070"+
"8248096050401448654283622417399764492353625350033374293733773767394279259525824"+
"7094916008735203948165670853233151776611528621199501507984793745085705740029921"+
"3547861466940296043254215190587755352673313992540129674205137541395491116851028"+
"0798423487758720503843109399736137255306088933126760017247953783675927135157722"+
"6102734929139407984301034177717780881549570661075010161916633401522789358679654"+
"9725203621287922655595366962817638879272680132431010476505963703947394957638906"+
"5729679296010090151251959509222435014093498712282479497471956469763185066761290"+
"6381105182419744486783638086174945516989279230187739107294578155431600500218284"+
"4096053772434203285478367015177394398700302370339518328690001558193988042707411"+
"5422278197165230110735658339673487176504919418123000406546931429992977795693031"+
"0050308630341856980323108369164002589297089098548682577736428825395492587362959"+
"6133298574739302373438847070370284412920166417850248733379080562754998434590761"+
"6431671031467107223700218107450444186647591348036690255324586254422253451813879"+
"1243457350136129778227828814894590986384600629316947188714958752549236649352047"+
"3243641097268276160877595088095126208404544477992299157248292516251278427659657"+
"0832146102982146179519579590959227042089896279712553632179488737642106606070659"+
"8256199010288075612519913751167821764361905705844078357350158005607745793421314"+
"49885007864151716151945");

        /** Natural logarithm of 2.
        * http://www.worldwideschool.org/library/books/sci/math/MiscellaneousMathematicalConstants/chap58.html
        */
        static BigDecimal LOG2 = new BigDecimal("0.693147180559945309417232121458176568075"+
"50013436025525412068000949339362196969471560586332699641868754200148102057068573"+
"368552023575813055703267075163507596193072757082837143519030703862389167347112335"+
"011536449795523912047517268157493206515552473413952588295045300709532636664265410"+
"423915781495204374043038550080194417064167151864471283996817178454695702627163106"+
"454615025720740248163777338963855069526066834113727387372292895649354702576265209"+
"885969320196505855476470330679365443254763274495125040606943814710468994650622016"+
"772042452452961268794654619316517468139267250410380254625965686914419287160829380"+
"317271436778265487756648508567407764845146443994046142260319309673540257444607030"+
"809608504748663852313818167675143866747664789088143714198549423151997354880375165"+
"861275352916610007105355824987941472950929311389715599820565439287170007218085761"+
"025236889213244971389320378439353088774825970171559107088236836275898425891853530"+
"243634214367061189236789192372314672321720534016492568727477823445353476481149418"+
"642386776774406069562657379600867076257199184734022651462837904883062033061144630"+
"073719489002743643965002580936519443041191150608094879306786515887090060520346842"+
"973619384128965255653968602219412292420757432175748909770675268711581705113700915"+
"894266547859596489065305846025866838294002283300538207400567705304678700184162404"+
"418833232798386349001563121889560650553151272199398332030751408426091479001265168"+
"243443893572472788205486271552741877243002489794540196187233980860831664811490930"+
"667519339312890431641370681397776498176974868903887789991296503619270710889264105"+
"230924783917373501229842420499568935992206602204654941510613");


        /** Euler's constant.
        * @param mc The required precision of the result.
        * @return 3.14159...
        * @since 2009-05-29
        */
        static public BigDecimal pi(final MathContext mc)
        {
                /* look it up if possible */
                if ( mc.getPrecision() < PI.precision() )
                        return PI.round(mc) ;
                else
                {
                        /* Broadhurst <a href="http://arxiv.org/abs/math/9803067">arXiv:math/9803067</a>
                        */
                        int[] a = {1,0,0,-1,-1,-1,0,0} ; 
                        BigDecimal S = broadhurstBBP(1,1,a,mc) ;
                        return multiplyRound(S,8) ;
                }
        } /* BigDecimalMath.pi */

        

        /** The square root.
        * @param x the non-negative argument.
        * @param mc
        * @return the square root of the BigDecimal.
        * @since 2008-10-27
        */
        static public BigDecimal sqrt(final BigDecimal x, final MathContext mc)
        {
                if ( x.compareTo(BigDecimal.ZERO) < 0 )
                        throw new ArithmeticException("negative argument "+x.toString()+ " of square root") ;
                if ( x.abs().subtract( new BigDecimal(Math.pow(10.,-mc.getPrecision())) ).compareTo(BigDecimal.ZERO) < 0 )
                        return BigDecimalMath.scalePrec(BigDecimal.ZERO,mc) ;
                /* start the computation from a double precision estimate */
                BigDecimal s = new BigDecimal( Math.sqrt(x.doubleValue()) ,mc) ;
                final BigDecimal half = new BigDecimal("2") ;

                /* increase the local accuracy by 2 digits */
                MathContext locmc = new MathContext(mc.getPrecision()+2,mc.getRoundingMode()) ;

                /* relative accuracy requested is 10^(-precision) 
                */
                final double eps = Math.pow(10.0,-mc.getPrecision()) ;
                for (;;)
                {
                        /* s = s -(s/2-x/2s); test correction s-x/s for being
                        * smaller than the precision requested. The relative correction is 1-x/s^2,
                        * (actually half of this, which we use for a little bit of additional protection).
                        */
                        if ( Math.abs(BigDecimal.ONE.subtract(x.divide(s.pow(2,locmc),locmc)).doubleValue()) < eps)
                                break ;
                        s = s.add(x.divide(s,locmc)).divide(half,locmc) ;
                        /* debugging
                        * System.out.println("itr "+x.round(locmc).toString() + " " + s.round(locmc).toString()) ;
                        */
                }
                return s ;
        } /* BigDecimalMath.sqrt */

        /** The square root.
        * @param x the non-negative argument.
        * @return the square root of the BigDecimal rounded to the precision implied by x.
        * @since 2009-06-25
        */
        static public BigDecimal sqrt(final BigDecimal x)
        {
                if ( x.compareTo(BigDecimal.ZERO) < 0 )
                        throw new ArithmeticException("negative argument "+x.toString()+ " of square root") ;

                return root(2,x) ;
        } /* BigDecimalMath.sqrt */

        /** The integer root.
        * @param n the positive argument.
        * @param x the non-negative argument.
        * @return The n-th root of the BigDecimal rounded to the precision implied by x, x^(1/n).
        * @since 2009-07-30
        */
        static public BigDecimal root(final int n, final BigDecimal x)
        {
                if ( x.compareTo(BigDecimal.ZERO) < 0 )
                        throw new ArithmeticException("negative argument "+x.toString()+ " of root") ;
                if ( n<= 0 )
                        throw new ArithmeticException("negative power "+ n + " of root") ;

                if ( n == 1 )
                        return x ;

                /* start the computation from a double precision estimate */
                BigDecimal s = new BigDecimal( Math.pow(x.doubleValue(),1.0/n) ) ;

                /* this creates nth with nominal precision of 1 digit
                */
                final BigDecimal nth = new BigDecimal(n) ;

                /* Specify an internal accuracy within the loop which is
                * slightly larger than what is demanded by 'eps' below.
                */
                final BigDecimal xhighpr = scalePrec(x,2) ;
                MathContext mc = new MathContext( 2+x.precision() ) ;

                /* Relative accuracy of the result is eps.
                */
                final double eps = x.ulp().doubleValue()/(2*n*x.doubleValue()) ;
                for (;;)
                {
                        /* s = s -(s/n-x/n/s^(n-1)) = s-(s-x/s^(n-1))/n; test correction s/n-x/s for being
                        * smaller than the precision requested. The relative correction is (1-x/s^n)/n,
                        */
                        BigDecimal c = xhighpr.divide( s.pow(n-1),mc ) ;
                        c = s.subtract(c) ;
                        MathContext locmc = new MathContext( c.precision() ) ;
                        c = c.divide(nth,locmc) ;
                        s = s. subtract(c) ;
                        if ( Math.abs( c.doubleValue()/s.doubleValue()) < eps)
                                break ;
                }
                return s.round(new MathContext( err2prec(eps)) ) ;
        } /* BigDecimalMath.root */

 

        /** A suggestion for the maximum numter of terms in the Taylor expansion of the exponential.
        */
        static private int TAYLOR_NTERM = 8 ;

        /** The exponential function.
        * @param x the argument.
        * @return exp(x).
        * The precision of the result is implicitly defined by the precision in the argument.
        * In particular this means that "Invalid Operation" errors are thrown if catastrophic
        * cancellation of digits causes the result to have no valid digits left.
        * @since 2009-05-29
        * @author Richard J. Mathar
        */
        static public BigDecimal exp(BigDecimal x)
        {
                /* To calculate the value if x is negative, use exp(-x) = 1/exp(x)
                */
                if ( x.compareTo(BigDecimal.ZERO) < 0 )
                {
                        final BigDecimal invx = exp(x.negate() ) ;
                        /* Relative error in inverse of invx is the same as the relative errror in invx.
                        * This is used to define the precision of the result.
                        */
                        MathContext mc = new MathContext( invx.precision() ) ;
                        return BigDecimal.ONE.divide( invx, mc ) ;
                }
                else if ( x.compareTo(BigDecimal.ZERO) == 0 )
                {
                        /* recover the valid number of digits from x.ulp(), if x hits the
                        * zero. The x.precision() is 1 then, and does not provide this information.
                        */
                        return scalePrec(BigDecimal.ONE, -(int)(Math.log10( x.ulp().doubleValue() )) ) ;
                }
                else
                {
                        /* Push the number in the Taylor expansion down to a small
                        * value where TAYLOR_NTERM terms will do. If x<1, the n-th term is of the order
                        * x^n/n!, and equal to both the absolute and relative error of the result
                        * since the result is close to 1. The x.ulp() sets the relative and absolute error
                        * of the result, as estimated from the first Taylor term.
                        * We want x^TAYLOR_NTERM/TAYLOR_NTERM! < x.ulp, which is guaranteed if
                        * x^TAYLOR_NTERM < TAYLOR_NTERM*(TAYLOR_NTERM-1)*...*x.ulp.
                        */
                        final double xDbl = x.doubleValue() ;
                        final double xUlpDbl = x.ulp().doubleValue() ;
                        if ( Math.pow(xDbl,TAYLOR_NTERM) < TAYLOR_NTERM*(TAYLOR_NTERM-1.0)*(TAYLOR_NTERM-2.0)*xUlpDbl ) 
                        {
                                /* Add TAYLOR_NTERM terms of the Taylor expansion (Euler's sum formula)
                                */
                                BigDecimal resul = BigDecimal.ONE ;

                                /* x^i */
                                BigDecimal xpowi = BigDecimal.ONE ;

                                /* i factorial */
                                BigInteger ifac = BigInteger.ONE ;

                                /* TAYLOR_NTERM terms to be added means we move x.ulp() to the right
                                * for each power of 10 in TAYLOR_NTERM, so the addition won't add noise beyond
                                * what's already in x.
                                */
                                MathContext mcTay = new MathContext( err2prec(1.,xUlpDbl/TAYLOR_NTERM) ) ;
                                for(int i=1 ; i <= TAYLOR_NTERM ; i++)
                                {
                                        ifac = ifac.multiply(new BigInteger(""+i) ) ;
                                        xpowi = xpowi.multiply(x) ;
                                        final BigDecimal c= xpowi.divide(new BigDecimal(ifac),mcTay)  ;
                                        resul = resul.add(c) ;
                                        if ( Math.abs(xpowi.doubleValue()) < i && Math.abs(c.doubleValue()) < 0.5* xUlpDbl )
                                                break;
                                }
                                /* exp(x+deltax) = exp(x)(1+deltax) if deltax is <<1. So the relative error
                                * in the result equals the absolute error in the argument.
                                */
                                MathContext mc = new MathContext( err2prec(xUlpDbl/2.) ) ;
                                return resul.round(mc) ;
                        }
                        else
                        {
                                /* Compute exp(x) = (exp(0.1*x))^10. Division by 10 does not lead
                                * to loss of accuracy.
                                */
                                int exSc = (int) ( 1.0-Math.log10( TAYLOR_NTERM*(TAYLOR_NTERM-1.0)*(TAYLOR_NTERM-2.0)*xUlpDbl
                                                        /Math.pow(xDbl,TAYLOR_NTERM) ) / ( TAYLOR_NTERM-1.0) ) ; 
                                BigDecimal xby10 = x.scaleByPowerOfTen(-exSc) ;
                                BigDecimal expxby10 = exp(xby10) ;

                                /* Final powering by 10 means that the relative error of the result
                                * is 10 times the relative error of the base (First order binomial expansion).
                                * This looses one digit.
                                */
                                MathContext mc = new MathContext( expxby10.precision()-exSc ) ;
                                /* Rescaling the powers of 10 is done in chunks of a maximum of 8 to avoid an invalid operation
                                * response by the BigDecimal.pow library or integer overflow.
                                */
                                while ( exSc > 0 )
                                {
                                        int exsub = Math.min(8,exSc) ;
                                        exSc -= exsub ;
                                        MathContext mctmp = new MathContext( expxby10.precision()-exsub+2 ) ;
                                        int pex = 1 ;
                                        while ( exsub-- > 0 )
                                                pex *= 10 ;
                                        expxby10 = expxby10.pow(pex,mctmp) ;
                                }
                                return expxby10.round(mc) ;
                        }
                }
        } /* BigDecimalMath.exp */

        /** The base of the natural logarithm.
        * @param mc the required precision of the result
        * @return exp(1) = 2.71828....
        * @since 2009-05-29
        */
        static public BigDecimal exp(final MathContext mc)
        {
                /* look it up if possible */
                if ( mc.getPrecision() < E.precision() )
                        return E.round(mc) ;
                else
                {
                        /* Instantiate a 1.0 with the requested pseudo-accuracy
                        * and delegate the computation to the public method above.
                        */
                        BigDecimal uni = scalePrec(BigDecimal.ONE, mc.getPrecision() ) ;
                        return exp(uni) ;
                }
        } /* BigDecimalMath.exp */

        /** The natural logarithm.
        * @param x the argument.
        * @return ln(x).
        * The precision of the result is implicitly defined by the precision in the argument.
        * @since 2009-05-29
        * @author Richard J. Mathar
        */
        static public BigDecimal log(BigDecimal x)
        {
                /* the value is undefined if x is negative.
                */
                if ( x.compareTo(BigDecimal.ZERO) < 0 )
                        throw new ArithmeticException("Cannot take log of negative "+ x.toString() ) ;
                else if ( x.compareTo(BigDecimal.ONE) == 0 )
                {
                        /* log 1. = 0. */
                        return scalePrec(BigDecimal.ZERO, x.precision()-1) ;
                }
                else if ( Math.abs(x.doubleValue()-1.0) <= 0.3 )
                {
                        /* The standard Taylor series around x=1, z=0, z=x-1. Abramowitz-Stegun 4.124.
                        * The absolute error is err(z)/(1+z) = err(x)/x.
                        */
                        BigDecimal z = scalePrec(x.subtract(BigDecimal.ONE),2) ;
                        BigDecimal zpown = z ;
                        double eps = 0.5*x.ulp().doubleValue()/Math.abs(x.doubleValue()) ;
                        BigDecimal resul = z ;
                        for(int k= 2;; k++)
                        {
                                zpown = multiplyRound(zpown,z) ;
                                BigDecimal c = divideRound(zpown,k) ;
                                if ( k % 2 == 0)
                                        resul = resul.subtract(c) ;
                                else
                                        resul = resul.add(c) ;
                                if ( Math.abs(c.doubleValue()) < eps)
                                        break;
                        }
                        MathContext mc = new MathContext( err2prec(resul.doubleValue(),eps) ) ;
                        return resul.round(mc) ;
                }
                else
                {
                        final double xDbl = x.doubleValue() ;
                        final double xUlpDbl = x.ulp().doubleValue() ;

                        /* Map log(x) = log root[r](x)^r = r*log( root[r](x)) with the aim
                        * to move roor[r](x) near to 1.2 (that is, below the 0.3 appearing above), where log(1.2) is roughly 0.2.
                        */
                        int r = (int) (Math.log(xDbl)/0.2) ;

                        /* Since the actual requirement is a function of the value 0.3 appearing above,
                        * we avoid the hypothetical case of endless recurrence by ensuring that r >= 2.
                        */
                        r = Math.max(2,r) ;

                        /* Compute r-th root with 2 additional digits of precision
                        */
                        BigDecimal xhighpr = scalePrec(x,2) ;
                        BigDecimal resul = root(r,xhighpr) ;
                        resul = log(resul).multiply(new BigDecimal(r)) ;

                        /* error propagation: log(x+errx) = log(x)+errx/x, so the absolute error
                        * in the result equals the relative error in the input, xUlpDbl/xDbl .
                        */
                        MathContext mc = new MathContext( err2prec(resul.doubleValue(),xUlpDbl/xDbl) ) ;
                        return resul.round(mc) ;
                }
        } /* BigDecimalMath.log */

        /** The natural logarithm.
        * @param n The main argument, a strictly positive integer.
        * @param mc The requirements on the precision.
        * @return ln(n).
        * @since 2009-08-08
        * @author Richard J. Mathar
        */
        static public BigDecimal log(int n, final MathContext mc)
        {
                /* the value is undefined if x is negative.
                */
                if ( n <= 0 )
                        throw new ArithmeticException("Cannot take log of negative "+ n ) ;
                else if ( n == 1)
                        return BigDecimal.ZERO ;
                else if ( n == 2)
                {
                        if ( mc.getPrecision() < LOG2.precision() )
                                return LOG2.round(mc) ;
                        else
                        {
                                /* Broadhurst <a href="http://arxiv.org/abs/math/9803067">arXiv:math/9803067</a>
                                * Error propagation: the error in log(2) is twice the error in S(2,-5,...).
                                */
                                int[] a = {2,-5,-2,-7,-2,-5,2,-3} ; 
                                BigDecimal S = broadhurstBBP(2,1,a, new MathContext(1+mc.getPrecision()) ) ;
                                S = S.multiply(new BigDecimal(8)) ;
                                S = sqrt(divideRound(S,3)) ;
                                return S.round(mc) ;
                        }
                }
                else if ( n == 3)
                {
                        /* summation of a series roughly proportional to (7/500)^k. Estimate count
                        * of terms to estimate the precision (drop the favorable additional
                        * 1/k here): 0.013^k <= 10^(-precision), so k*log10(0.013) <= -precision
                        * so k>= precision/1.87.
                        */
                        int kmax = (int)(mc.getPrecision()/1.87) ;
                        MathContext mcloc = new MathContext( mc.getPrecision()+ 1+(int)(Math.log10(kmax*0.693/1.098)) ) ;
                        BigDecimal log3 = multiplyRound( log(2,mcloc),19 ) ;

                        /* log3 is roughly 1, so absolute and relative error are the same. The
                        * result will be divided by 12, so a conservative error is the one
                        * already found in mc
                        */
                        double eps = prec2err(1.098,mc.getPrecision() )/kmax ;
                        Rational r = new Rational(7153,524288) ;
                        Rational pk = new Rational(7153,524288) ;
                        for(int k=1; ; k++)
                        {
                                Rational tmp = pk.divide(k) ;
                                if ( tmp.doubleValue() < eps)
                                        break ;

                                /* how many digits of tmp do we need in the sum?
                                */
                                mcloc = new MathContext( err2prec(tmp.doubleValue(),eps) ) ;
                                BigDecimal c = pk.divide(k).BigDecimalValue(mcloc) ;
                                if ( k % 2 != 0)
                                        log3 = log3.add(c) ;
                                else
                                        log3 = log3.subtract(c) ;
                                pk = pk.multiply(r) ;
                        }
                        log3 = divideRound( log3,12 ) ;
                        return log3.round(mc) ;
                }
                else if ( n == 5)
                {
                        /* summation of a series roughly proportional to (7/160)^k. Estimate count
                        * of terms to estimate the precision (drop the favorable additional
                        * 1/k here): 0.046^k <= 10^(-precision), so k*log10(0.046) <= -precision
                        * so k>= precision/1.33.
                        */
                        int kmax = (int)(mc.getPrecision()/1.33) ;
                        MathContext mcloc = new MathContext( mc.getPrecision()+ 1+(int)(Math.log10(kmax*0.693/1.609)) ) ;
                        BigDecimal log5 = multiplyRound( log(2,mcloc),14 ) ;

                        /* log5 is roughly 1.6, so absolute and relative error are the same. The
                        * result will be divided by 6, so a conservative error is the one
                        * already found in mc
                        */
                        double eps = prec2err(1.6,mc.getPrecision() )/kmax ;
                        Rational r = new Rational(759,16384) ;
                        Rational pk = new Rational(759,16384) ;
                        for(int k=1; ; k++)
                        {
                                Rational tmp = pk.divide(k) ;
                                if ( tmp.doubleValue() < eps)
                                        break ;

                                /* how many digits of tmp do we need in the sum?
                                */
                                mcloc = new MathContext( err2prec(tmp.doubleValue(),eps) ) ;
                                BigDecimal c = pk.divide(k).BigDecimalValue(mcloc) ;
                                log5 = log5.subtract(c) ;
                                pk = pk.multiply(r) ;
                        }
                        log5 = divideRound( log5,6 ) ;
                        return log5.round(mc) ;
                }
                else if ( n == 7)
                {
                        /* summation of a series roughly proportional to (1/8)^k. Estimate count
                        * of terms to estimate the precision (drop the favorable additional
                        * 1/k here): 0.125^k <= 10^(-precision), so k*log10(0.125) <= -precision
                        * so k>= precision/0.903.
                        */
                        int kmax = (int)(mc.getPrecision()/0.903) ;
                        MathContext mcloc = new MathContext( mc.getPrecision()+ 1+(int)(Math.log10(kmax*3*0.693/1.098)) ) ;
                        BigDecimal log7 = multiplyRound( log(2,mcloc),3 ) ;

                        /* log7 is roughly 1.9, so absolute and relative error are the same.
                        */
                        double eps = prec2err(1.9,mc.getPrecision() )/kmax ;
                        Rational r = new Rational(1,8) ;
                        Rational pk = new Rational(1,8) ;
                        for(int k=1; ; k++)
                        {
                                Rational tmp = pk.divide(k) ;
                                if ( tmp.doubleValue() < eps)
                                        break ;

                                /* how many digits of tmp do we need in the sum?
                                */
                                mcloc = new MathContext( err2prec(tmp.doubleValue(),eps) ) ;
                                BigDecimal c = pk.divide(k).BigDecimalValue(mcloc) ;
                                log7 = log7.subtract(c) ;
                                pk = pk.multiply(r) ;
                        }
                        return log7.round(mc) ;

                }

                else
                {
                        /* At this point one could either forward to the log(BigDecimal) signature (implemented)
                        * or decompose n into Ifactors and use an implemenation of all the prime bases.
                        * Estimate of the result; convert the mc argument to an  absolute error eps
                        * log(n+errn) = log(n)+errn/n = log(n)+eps
                        */
                        double res = Math.log((double)n) ;
                        double eps = prec2err(res,mc.getPrecision() ) ;
                        /* errn = eps*n, convert absolute error in result to requirement on absolute error in input
                        */
                        eps *= n ;
                        /* Convert this absolute requirement of error in n to a relative error in n
                        */
                        final MathContext mcloc = new MathContext( 1+err2prec((double)n,eps ) ) ;
                        /* Padd n with a number of zeros to trigger the required accuracy in
                        * the standard signature method
                        */
                        BigDecimal nb = scalePrec(new BigDecimal(n),mcloc) ;
                        return log(nb) ;
                }
        } /* log */

        /** The natural logarithm.
        * @param r The main argument, a strictly positive value.
        * @param mc The requirements on the precision.
        * @return ln(r).
        * @since 2009-08-09
        * @author Richard J. Mathar
        */
        static public BigDecimal log(final Rational r, final MathContext mc)
        {
                /* the value is undefined if x is negative.
                */
                if ( r.compareTo(Rational.ZERO) <= 0 )
                        throw new ArithmeticException("Cannot take log of negative "+ r.toString() ) ;
                else if ( r.compareTo(Rational.ONE) == 0)
                        return BigDecimal.ZERO ;
                else
                {

                        /* log(r+epsr) = log(r)+epsr/r. Convert the precision to an absolute error in the result.
                        * eps contains the required absolute error of the result, epsr/r.
                        */
                        double eps = prec2err( Math.log(r.doubleValue()), mc.getPrecision()) ;

                        /* Convert this further into a requirement of the relative precision in r, given that
                        * epsr/r is also the relative precision of r. Add one safety digit.
                        */
                        MathContext mcloc = new MathContext( 1+err2prec(eps)  ) ;

                        final BigDecimal resul = log( r.BigDecimalValue(mcloc) );

                        return resul.round(mc) ;
                }
        } /* log */

        /** Power function.
        * @param x Base of the power.
        * @param y Exponent of the power.
        * @return x^y.
        *  The estimation of the relative error in the result is |log(x)*err(y)|+|y*err(x)/x|
        * @since 2009-06-01
        */
        static public BigDecimal pow(final BigDecimal x, final BigDecimal y)
        {
                if( x.compareTo(BigDecimal.ZERO) < 0 )
                        throw new ArithmeticException("Cannot power negative "+ x.toString()) ;
                else if( x.compareTo(BigDecimal.ZERO) == 0 )
                        return BigDecimal.ZERO ;
                else
                {
                        /* return x^y = exp(y*log(x)) ;
                        */
                        BigDecimal logx = log(x) ;
                        BigDecimal ylogx = y.multiply(logx) ;
                        BigDecimal resul = exp(ylogx) ;

                        /* The estimation of the relative error in the result is |log(x)*err(y)|+|y*err(x)/x| 
                        */
                        double errR = Math.abs(logx.doubleValue()*y.ulp().doubleValue()/2.)
                                + Math.abs(y.doubleValue()*x.ulp().doubleValue()/2./x.doubleValue()) ;
                        MathContext mcR = new MathContext( err2prec(1.0,errR) ) ;
                        return resul.round(mcR) ;
                }
        } /* BigDecimalMath.pow */

        /** Raise to an integer power and round.
        * @param x The base.
        * @param n The exponent.
        * @return x^n.
        * @since 2009-08-13
        * @since 2010-05-26 handle also n<0 cases.
        */
        static public BigDecimal powRound(final BigDecimal x, final int n)
        {
                /** Special cases: x^1=x and x^0 = 1
                */
                if ( n == 1 )
                        return x;
                else if ( n == 0 )
                        return BigDecimal.ONE ;
                else
                {
                        /* The relative error in the result is n times the relative error in the input.
                        * The estimation is slightly optimistic due to the integer rounding of the logarithm.
                        * Since the standard BigDecimal.pow can only handle positive n, we split the algorithm.
                        */
                        MathContext mc = new MathContext( x.precision() - (int)Math.log10((double)(Math.abs(n))) ) ;
                        if ( n > 0 )
                                return x.pow(n,mc) ;
                        else
                                return BigDecimal.ONE.divide( x.pow(-n),mc) ;
                }
        } /* BigDecimalMath.powRound */

        /** Raise to an integer power and round.
        * @param x The base.
        * @param n The exponent.
        *   The current implementation allows n only in the interval of the standard int values.
        * @return x^n.
        * @since 2010-05-26
        */
        static public BigDecimal powRound(final BigDecimal x, final BigInteger n)
        {
                /** For now, the implementation forwards to the cases where n
                * is in the range of the standard integers. This might, however, be
                * implemented to decompose larger powers into cascaded calls to smaller ones.
                */
                if ( n.compareTo(Rational.MAX_INT) > 0 || n.compareTo(Rational.MIN_INT) < 0)
                        throw new ProviderException("Not implemented: big power "+n.toString() ) ;
                else 
                        return powRound(x,n.intValue() ) ;
        } /* BigDecimalMath.powRound */

        /** Raise to a fractional power and round.
        * @param x The base.
        *     Generally enforced to be positive, with the exception of integer exponents where
        *     the sign is carried over according to the parity of the exponent.
        * @param q The exponent.
        * @return x^q.
        * @since 2010-05-26
        */
        static public BigDecimal powRound(final BigDecimal x, final Rational q)
        {
                /** Special cases: x^1=x and x^0 = 1
                */
                if ( q.compareTo(BigInteger.ONE) == 0 )
                        return x;
                else if ( q.signum() == 0 )
                        return BigDecimal.ONE ;
                else if ( q.isInteger() )
                {
                        /* We are sure that the denominator is positive here, because normalize() has been
                        * called during constrution etc.
                        */
                                return powRound(x,q.a) ;
                }
                        /* Refuse to operate on the general negative basis. The integer q have already been handled above.
                        */
                else if ( x.compareTo(BigDecimal.ZERO) < 0 )
                        throw new ArithmeticException("Cannot power negative "+ x.toString() ) ;
                else
                {
                        if ( q.isIntegerFrac() )
                        {
                                /* Newton method with first estimate in double precision.
                                * The disadvantage of this first line here is that the result must fit in the
                                * standard range of double precision numbers exponents.
                                */
                                double estim = Math.pow( x.doubleValue(),q.doubleValue() ) ;
                                BigDecimal res = new BigDecimal(estim) ;

                                /* The error in x^q is q*x^(q-1)*Delta(x).
                                * The relative error is q*Delta(x)/x, q times the relative error of x.
                                */
                                BigDecimal reserr = new BigDecimal( 0.5* q.abs().doubleValue() 
                                                                * x.ulp().divide(x.abs(),MathContext.DECIMAL64).doubleValue() ) ;

                                /* The main point in branching the cases above is that this conversion
                                * will succeed for numerator and denominator of q.
                                */
                                int qa = q.a.intValue() ;
                                int qb = q.b.intValue() ;

                                /* Newton iterations. */
                                BigDecimal xpowa = powRound(x, qa) ;
                                for( ;; )
                                {
                                        /* numerator and denominator of the Newton term.  The major
                                        * disadvantage of this implementation is that the updates of the powers
                                        * of the new estimate are done in full precision calling BigDecimal.pow(),
                                        * which becomes slow if the denominator of q is large.
                                        */
                                        BigDecimal nu = res.pow(qb) .subtract(xpowa) ;
                                        BigDecimal de = multiplyRound( res.pow(qb-1),q.b) ;

                                        /* estimated correction */
                                        BigDecimal eps = nu.divide(de,MathContext.DECIMAL64) ;

                                        BigDecimal err = res.multiply(reserr,MathContext.DECIMAL64) ;
                                        int precDiv = 2+err2prec(eps,err) ;
                                        if ( precDiv <= 0 )
                                        {
                                                /* The case when the precision is already reached and any precision
                                                * will do. */
                                                eps = nu.divide(de,MathContext.DECIMAL32) ;
                                        }
                                        else
                                        {
                                                MathContext mc = new MathContext(precDiv) ;
                                                eps = nu.divide(de,mc) ;
                                        }

                                        res = subtractRound(res,eps) ;
                                        /* reached final precision if the relative error fell below reserr,
                                        * |eps/res| < reserr
                                        */
                                        if ( eps.divide(res,MathContext.DECIMAL64).abs().compareTo(reserr) < 0 )
                                        {
                                                /* delete the bits of extra precision kept in this
                                                * working copy.
                                                */
                                                MathContext mc = new MathContext(err2prec(reserr.doubleValue())) ;
                                                return res.round(mc) ;
                                        }
                                }
                        }
                        else
                        {
                                /* The error in x^q is q*x^(q-1)*Delta(x) + Delta(q)*x^q*log(x).
                                * The relative error is q/x*Delta(x) + Delta(q)*log(x). Convert q to a floating point
                                * number such that its relative error becomes negligible: Delta(q)/q << Delta(x)/x/log(x) .
                                */
                                int precq =  3+err2prec( (x.ulp().divide(x,MathContext.DECIMAL64)).doubleValue() 
                                                        / Math.log(x.doubleValue()) ) ;
                                MathContext mc = new MathContext(precq) ;

                                /* Perform the actual calculation as exponentiation of two floating point numbers.
                                */
                                return pow(x, q.BigDecimalValue(mc) ) ;
                        }


                }
        } /* BigDecimalMath.powRound */

       
        /** The inverse trigonometric sine.
        * @param x the argument.
        * @return the arcsin(x) in radians.
        */
        static public BigDecimal asin(final BigDecimal x)
        {
                if ( x.compareTo(BigDecimal.ONE) > 0 || x.compareTo(BigDecimal.ONE.negate()) < 0 )
                {
                        throw new ArithmeticException("Out of range argument "+ x.toString() + " of asin") ;
                }
                else if ( x.compareTo(BigDecimal.ZERO) == 0 )
                        return BigDecimal.ZERO ;
                else if ( x.compareTo(BigDecimal.ONE) == 0 )
                {
                        /* arcsin(1) = pi/2
                        */
                        double errpi = Math.sqrt(x.ulp().doubleValue()) ;
                        MathContext mc = new MathContext( err2prec(3.14159,errpi) ) ;
                        return pi(mc).divide(new BigDecimal(2)) ;
                }
                else if ( x.compareTo(BigDecimal.ZERO) < 0 )
                {
                        return asin(x.negate()).negate() ;
                }
                else if ( x.doubleValue() > 0.7)
                {
                        final BigDecimal xCompl = BigDecimal.ONE.subtract(x) ;
                        final double xDbl = x.doubleValue() ;
                        final double xUlpDbl = x.ulp().doubleValue()/2. ;
                        final double eps = xUlpDbl/2./Math.sqrt(1.-Math.pow(xDbl,2.)) ;

                        final BigDecimal xhighpr = scalePrec(xCompl,3) ;
                        final BigDecimal xhighprV = divideRound(xhighpr,4) ;

                        BigDecimal resul = BigDecimal.ONE ;

                        /* x^(2i+1) */
                        BigDecimal xpowi = BigDecimal.ONE ;

                        /* i factorial */
                        BigInteger ifacN = BigInteger.ONE ;
                        BigInteger ifacD = BigInteger.ONE ;

                        for(int i=1 ; ; i++)
                        {
                                ifacN = ifacN.multiply(new BigInteger(""+(2*i-1)) ) ;
                                ifacD = ifacD.multiply(new BigInteger(""+i) ) ;
                                if ( i == 1)
                                        xpowi = xhighprV ;
                                else
                                        xpowi = multiplyRound(xpowi,xhighprV) ;
                                BigDecimal c = divideRound( multiplyRound(xpowi,ifacN),
                                                                ifacD.multiply(new BigInteger(""+(2*i+1)) ) ) ;
                                resul = resul.add(c) ;
                                /* series started 1+x/12+... which yields an estimate of the sum's error
                                */
                                if ( Math.abs(c.doubleValue()) < xUlpDbl/120.) 
                                        break;
                        }
                        /* sqrt(2*z)*(1+...)
                        */
                        xpowi = sqrt(xhighpr.multiply(new BigDecimal(2))) ;
                        resul = multiplyRound(xpowi,resul) ;

                        MathContext mc = new MathContext( resul.precision() ) ;
                        BigDecimal pihalf = pi(mc).divide(new BigDecimal(2)) ;

                        mc = new MathContext( err2prec(resul.doubleValue(),eps) ) ;
                        return pihalf.subtract(resul,mc) ;
                }
                else
                {
                        /* absolute error in the result is err(x)/sqrt(1-x^2) to lowest order
                        */
                        final double xDbl = x.doubleValue() ;
                        final double xUlpDbl = x.ulp().doubleValue()/2. ;
                        final double eps = xUlpDbl/2./Math.sqrt(1.-Math.pow(xDbl,2.)) ;

                        final BigDecimal xhighpr = scalePrec(x,2) ;
                        final BigDecimal xhighprSq = multiplyRound(xhighpr,xhighpr) ;

                        BigDecimal resul = xhighpr.plus() ;

                        /* x^(2i+1) */
                        BigDecimal xpowi = xhighpr ;

                        /* i factorial */
                        BigInteger ifacN = BigInteger.ONE ;
                        BigInteger ifacD = BigInteger.ONE ;

                        for(int i=1 ; ; i++)
                        {
                                ifacN = ifacN.multiply(new BigInteger(""+(2*i-1)) ) ;
                                ifacD = ifacD.multiply(new BigInteger(""+(2*i)) ) ;
                                xpowi = multiplyRound(xpowi,xhighprSq) ;
                                BigDecimal c = divideRound( multiplyRound(xpowi,ifacN),
                                                                ifacD.multiply(new BigInteger(""+(2*i+1)) ) ) ;
                                resul = resul.add(c) ;
                                if ( Math.abs(c.doubleValue()) < 0.1*eps) 
                                        break;
                        }
                        MathContext mc = new MathContext( err2prec(resul.doubleValue(),eps) ) ;
                        return resul.round(mc) ;
                }
        } /* BigDecimalMath.asin */

        /** The inverse trigonometric cosine.
        * @param x the argument.
        * @return the arccos(x) in radians.
        * @since 2009-09-29
        */
        static public BigDecimal acos(final BigDecimal x)
        {
                /* Essentially forwarded to pi/2 - asin(x)
                */
                final BigDecimal xhighpr = scalePrec(x,2) ;
                BigDecimal resul = asin(xhighpr) ;
                double eps = resul.ulp().doubleValue()/2. ;

                MathContext mc = new MathContext( err2prec(3.14159,eps) ) ;
                BigDecimal pihalf = pi(mc).divide(new BigDecimal(2)) ;
                resul = pihalf.subtract(resul) ;

                /* absolute error in the result is err(x)/sqrt(1-x^2) to lowest order
                */
                final double xDbl = x.doubleValue() ;
                final double xUlpDbl = x.ulp().doubleValue()/2. ;
                eps = xUlpDbl/2./Math.sqrt(1.-Math.pow(xDbl,2.)) ;

                mc = new MathContext( err2prec(resul.doubleValue(),eps) ) ;
                return resul.round(mc) ;

        } /* BigDecimalMath.acos */


        /** The hyperbolic tangent.
        * @param x The argument.
        * @return The tanh(x) = sinh(x)/cosh(x).
        * @author Richard J. Mathar
        * @since 2009-08-20
        */
        static public BigDecimal tanh(final BigDecimal x)
        {
                if ( x.compareTo(BigDecimal.ZERO) < 0)
                        return tanh(x.negate()).negate() ;
                else if ( x.compareTo(BigDecimal.ZERO) == 0 )
                        return BigDecimal.ZERO ;
                else
                {
                        BigDecimal xhighpr = scalePrec(x,2) ;

                        /* tanh(x) = (1-e^(-2x))/(1+e^(-2x)) .
                        */
                        BigDecimal exp2x = exp( xhighpr.multiply(new BigDecimal(-2)) ) ;

                        /* The error in tanh x is err(x)/cosh^2(x).
                        */
                        double eps = 0.5*x.ulp().doubleValue()/Math.pow( Math.cosh(x.doubleValue()), 2.0 ) ;
                        MathContext mc = new MathContext( err2prec(Math.tanh(x.doubleValue()),eps) ) ;
                        return BigDecimal.ONE.subtract(exp2x).divide( BigDecimal.ONE.add(exp2x), mc) ;
                }
        } /* BigDecimalMath.tanh */

       
        /** trigonometric cot.
        * @param x The argument.
        * @return cot(x) = 1/tan(x).
        */
        static public double cot(final double x)
        {
                return 1./Math.tan(x) ;
        }
        /** Broadhurst ladder sequence.
        * @param a The vector of 8 integer arguments
        * @param mc Specification of the accuracy of the result
        * @return S_(n,p)(a)
        * @since 2009-08-09
        * @see <a href="http://arxiv.org/abs/math/9803067">arXiv:math/9803067</a>
        */
        static protected BigDecimal broadhurstBBP(final int n, final int p, final int a[], MathContext mc)
        {
                /* Explore the actual magnitude of the result first with a quick estimate.
                */
                double x = 0.0 ;
                for(int k=1; k < 10 ; k++)
                        x += a[ (k-1) % 8]/Math.pow(2., p*(k+1)/2)/Math.pow((double)k,n) ;

                /* Convert the relative precision and estimate of the result into an absolute precision.
                */
                double eps = prec2err(x,mc.getPrecision()) ;

                /* Divide this through the number of terms in the sum to account for error accumulation
                * The divisor 2^(p(k+1)/2) means that on the average each 8th term in k has shrunk by
                * relative to the 8th predecessor by 1/2^(4p).  1/2^(4pc) = 10^(-precision) with c the 8term
                * cycles yields c=log_2( 10^precision)/4p = 3.3*precision/4p  with k=8c
                */
                int kmax= (int)(6.6*mc.getPrecision()/p) ;

                /* Now eps is the absolute error in each term */
                eps /= kmax ;
                BigDecimal res = BigDecimal.ZERO ;
                for(int c =0 ; ; c++)
                {
                        Rational r = new Rational() ;
                        for (int k=0; k < 8 ; k++)
                        {
                                Rational tmp = new Rational(new BigInteger(""+a[k]),(new BigInteger(""+(1+8*c+k))).pow(n)) ;
                                /* floor( (pk+p)/2)
                                */
                                int pk1h = p*(2+8*c+k)/2 ;
                                tmp = tmp.divide( BigInteger.ONE.shiftLeft(pk1h) ) ;
                                r = r.add(tmp) ;
                        }
        
                        if ( Math.abs(r.doubleValue()) < eps)
                                break;
                        MathContext mcloc = new MathContext( 1+err2prec(r.doubleValue(),eps) ) ;
                        res = res.add( r.BigDecimalValue(mcloc) ) ;
                }
                return res.round(mc) ;
        } /* broadhurstBBP */








        /** Add a BigDecimal and a BigInteger.
        * @param x The left summand
        * @param y The right summand
        * @return The sum x+y.
        * @since 2012-03-02
        */
        static public BigDecimal add(final BigDecimal x, final BigInteger y)
        {
                return x.add(new BigDecimal(y)) ;
        } /* add */


        /** Add and round according to the larger of the two ulp's.
        * @param x The left summand
        * @param y The right summand
        * @return The sum x+y.
        * @since 2009-07-30
        */
        static public BigDecimal addRound(final BigDecimal x, final BigDecimal y)
        {
                BigDecimal resul = x.add(y) ;
                /* The estimation of the absolute error in the result is |err(y)|+|err(x)| 
                */
                double errR = Math.abs( y.ulp().doubleValue()/2. ) + Math.abs( x.ulp().doubleValue()/2. ) ;
                MathContext mc = new MathContext( err2prec(resul.doubleValue(),errR) ) ;
                return resul.round(mc) ;
        } /* addRound */

        /** Add and round according to the larger of the two ulp's.
        * @param x The left summand
        * @param y The right summand
        * @return The sum x+y.
        * @since 2010-07-19
        */
        static public BigComplex addRound(final BigComplex x, final BigDecimal y)
        {
                final BigDecimal R = addRound(x.re,y) ;
                return new BigComplex(R,x.im) ;
        } /* addRound */

        /** Add and round according to the larger of the two ulp's.
        * @param x The left summand
        * @param y The right summand
        * @return The sum x+y.
        * @since 2010-07-19
        */
        static public BigComplex addRound(final BigComplex x, final BigComplex y)
        {
                final BigDecimal R = addRound(x.re,y.re) ;
                final BigDecimal I = addRound(x.im,y.im) ;
                return new BigComplex(R,I) ;
        } /* addRound */

        /** Subtract and round according to the larger of the two ulp's.
        * @param x The left term.
        * @param y The right term.
        * @return The difference x-y.
        * @since 2009-07-30
        */
        static public BigDecimal subtractRound(final BigDecimal x, final BigDecimal y)
        {
                BigDecimal resul = x.subtract(y) ;
                /* The estimation of the absolute error in the result is |err(y)|+|err(x)| 
                */
                double errR = Math.abs( y.ulp().doubleValue()/2. ) + Math.abs( x.ulp().doubleValue()/2. ) ;
                MathContext mc = new MathContext( err2prec(resul.doubleValue(),errR) ) ;
                return resul.round(mc) ;
        } /* subtractRound */

        /** Subtract and round according to the larger of the two ulp's.
        * @param x The left summand
        * @param y The right summand
        * @return The difference x-y.
        * @since 2010-07-19
        */
        static public BigComplex subtractRound(final BigComplex x, final BigComplex y)
        {
                final BigDecimal R = subtractRound(x.re,y.re) ;
                final BigDecimal I = subtractRound(x.im,y.im) ;
                return new BigComplex(R,I) ;
        } /* subtractRound */

        /** Multiply and round.
        * @param x The left factor.
        * @param y The right factor.
        * @return The product x*y.
        * @since 2009-07-30
        */
        static public BigDecimal multiplyRound(final BigDecimal x, final BigDecimal y)
        {
                BigDecimal resul = x.multiply(y) ;
                /* The estimation of the relative error in the result is the sum of the relative
                * errors |err(y)/y|+|err(x)/x| 
                */
                MathContext mc = new MathContext( Math.min(x.precision(),y.precision()) ) ;
                return resul.round(mc) ;
        } /* multiplyRound */

        /** Multiply and round.
        * @param x The left factor.
        * @param y The right factor.
        * @return The product x*y.
        * @since 2010-07-19
        */
        static public BigComplex multiplyRound(final BigComplex x, final BigDecimal y)
        {
                BigDecimal R = multiplyRound(x.re,y) ;
                BigDecimal I = multiplyRound(x.im,y) ;
                return new BigComplex(R,I) ;
        } /* multiplyRound */

        /** Multiply and round.
        * @param x The left factor.
        * @param y The right factor.
        * @return The product x*y.
        * @since 2010-07-19
        */
        static public BigComplex multiplyRound(final BigComplex x, final BigComplex y)
        {
                BigDecimal R = subtractRound(multiplyRound(x.re,y.re), multiplyRound(x.im,y.im)) ;
                BigDecimal I = addRound(multiplyRound(x.re,y.im), multiplyRound(x.im,y.re)) ;
                return new BigComplex(R,I) ;
        } /* multiplyRound */

        /** Multiply and round.
        * @param x The left factor.
        * @param f The right factor.
        * @return The product x*f.
        * @since 2009-07-30
        */
        static public BigDecimal multiplyRound(final BigDecimal x, final Rational f)
        {
                if (  f.compareTo(BigInteger.ZERO) == 0 ) 
                        return BigDecimal.ZERO ;
                else
                {
                        /* Convert the rational value with two digits of extra precision
                        */
                        MathContext mc = new MathContext( 2+x.precision() ) ;
                        BigDecimal fbd = f.BigDecimalValue(mc) ;

                        /* and the precision of the product is then dominated by the precision in x
                        */
                        return multiplyRound(x,fbd) ;
                }
        }

        /** Multiply and round.
        * @param x The left factor.
        * @param n The right factor.
        * @return The product x*n.
        * @since 2009-07-30
        */
        static public BigDecimal multiplyRound(final BigDecimal x, final int n)
        {
                BigDecimal resul = x.multiply(new BigDecimal(n)) ;
                /* The estimation of the absolute error in the result is |n*err(x)|
                */
                MathContext mc = new MathContext( n != 0 ? x.precision(): 0 ) ;
                return resul.round(mc) ;
        }

        /** Multiply and round.
        * @param x The left factor.
        * @param n The right factor.
        * @return the product x*n
        * @since 2009-07-30
        */
        static public BigDecimal multiplyRound(final BigDecimal x, final BigInteger n)
        {
                BigDecimal resul = x.multiply(new BigDecimal(n)) ;
                /* The estimation of the absolute error in the result is |n*err(x)|
                */
                MathContext mc = new MathContext( n.compareTo(BigInteger.ZERO) != 0 ? x.precision(): 0 ) ;
                return resul.round(mc) ;
        }

        /** Divide and round.
        * @param x The numerator
        * @param y The denominator
        * @return the divided x/y
        * @since 2009-07-30
        */
        static public BigDecimal divideRound(final BigDecimal x, final BigDecimal y)
        {
                /* The estimation of the relative error in the result is |err(y)/y|+|err(x)/x| 
                */
                MathContext mc = new MathContext( Math.min(x.precision(),y.precision()) ) ;
                BigDecimal resul = x.divide(y,mc) ;
                /* If x and y are precise integer values that may have common factors,
                * the method above will truncate trailing zeros, which may result in
                * a smaller apparent accuracy than starte... add missing trailing zeros now.
                */
                return scalePrec(resul,mc) ;
        }

        /** Build the inverse and maintain the approximate accuracy.
        * @param z The denominator
        * @return The divided 1/z = [Re(z)-i*Im(z)]/ [Re^2 z + Im^2 z]
        * @since 2010-07-19
        */
        static public BigComplex invertRound(final BigComplex z)
        {
                if (z.im.compareTo(BigDecimal.ZERO) == 0)
                {
                        /* In this case with vanishing Im(x), the result is  simply 1/Re z.
                        */
                        final MathContext mc = new MathContext( z.re.precision() ) ;
                        return new BigComplex( BigDecimal.ONE.divide( z.re, mc) ) ;
                }
                else if (z.re.compareTo(BigDecimal.ZERO) == 0)
                {
                        /* In this case with vanishing Re(z), the result is  simply -i/Im z
                        */
                        final MathContext mc = new MathContext( z.im.precision() ) ;
                        return new BigComplex(BigDecimal.ZERO, BigDecimal.ONE.divide( z.im, mc).negate() ) ;
                }
                else 
                {
                        /* 1/(x.re+I*x.im) = 1/(x.re+x.im^2/x.re) - I /(x.im +x.re^2/x.im)
                        */
                        BigDecimal R  = addRound(z.re, divideRound(multiplyRound(z.im,z.im), z.re) ) ;
                        BigDecimal I  = addRound(z.im, divideRound(multiplyRound(z.re,z.re), z.im) ) ;
                        MathContext mc = new MathContext( 1+R.precision() ) ;
                        R = BigDecimal.ONE.divide(R,mc) ;
                        mc = new MathContext( 1+I.precision() ) ;
                        I = BigDecimal.ONE.divide(I,mc) ;
                        return new BigComplex(R,I.negate()) ;
                }
        }

        /** Divide and round.
        * @param x The numerator
        * @param y The denominator
        * @return the divided x/y
        * @since 2010-07-19
        */
        static public BigComplex divideRound(final BigComplex x, final BigComplex y)
        {
                return multiplyRound( x, invertRound(y) ) ;
        }

        /** Divide and round.
        * @param x The numerator
        * @param n The denominator
        * @return the divided x/n
        * @since 2009-07-30
        */
        static public BigDecimal divideRound(final BigDecimal x, final int n)
        {
                /* The estimation of the relative error in the result is |err(x)/x| 
                */
                MathContext mc = new MathContext( x.precision() ) ;
                return x.divide(new BigDecimal(n),mc) ;
        }

        /** Divide and round.
        * @param x The numerator
        * @param n The denominator
        * @return the divided x/n
        * @since 2009-07-30
        */
        static public BigDecimal divideRound(final BigDecimal x, final BigInteger n)
        {
                /* The estimation of the relative error in the result is |err(x)/x| 
                */
                MathContext mc = new MathContext( x.precision() ) ;
                return x.divide(new BigDecimal(n),mc) ;
        } /* divideRound */

        /** Divide and round.
        * @param n The numerator
        * @param x The denominator
        * @return the divided n/x
        * @since 2009-08-05
        */
        static public BigDecimal divideRound(final BigInteger n, final BigDecimal x)
        {
                /* The estimation of the relative error in the result is |err(x)/x| 
                */
                MathContext mc = new MathContext( x.precision() ) ;
                return new BigDecimal(n).divide(x,mc) ;
        } /* divideRound */

        /** Divide and round.
        * @param n The numerator
        * @param x The denominator
        * @return the divided n/x
        * @since 2012-03-01
        */
        static public BigComplex divideRound(final BigInteger n, final BigComplex x)
        {
                /* catch case of real-valued denominator first
                */
                if ( x.im.compareTo(BigDecimal.ZERO) == 0 )
                        return new BigComplex( divideRound(n,x.re),BigDecimal.ZERO ) ;
                else if ( x.re.compareTo(BigDecimal.ZERO) == 0 )
                        return new BigComplex( BigDecimal.ZERO, divideRound(n,x.im).negate() ) ;
                        
                BigComplex z = invertRound(x) ;
                /* n/(x+iy) = nx/(x^2+y^2) -nyi/(x^2+y^2)       
                */
                BigDecimal repart = multiplyRound(z.re, n) ;
                BigDecimal impart = multiplyRound(z.im, n) ;
                return new BigComplex( repart, impart) ;
        } /* divideRound */

        /** Divide and round.
        * @param n The numerator.
        * @param x The denominator.
        * @return the divided n/x.
        * @since 2009-08-05
        */
        static public BigDecimal divideRound(final int n, final BigDecimal x)
        {
                /* The estimation of the relative error in the result is |err(x)/x| 
                */
                MathContext mc = new MathContext( x.precision() ) ;
                return new BigDecimal(n).divide(x,mc) ;
        }

        /** Append decimal zeros to the value. This returns a value which appears to have
        * a higher precision than the input.
        * @param x The input value
        * @param d The (positive) value of zeros to be added as least significant digits.
        * @return The same value as the input but with increased (pseudo) precision.
        */
        static public BigDecimal scalePrec(final BigDecimal x, int d)
        {
                return x.setScale(d+x.scale()) ;
        }

        /** Append decimal zeros to the value. This returns a value which appears to have
        * a higher precision than the input.
        * @param x The input value
        * @param d The (positive) value of zeros to be added as least significant digits.
        * @return The same value as the input but with increased (pseudo) precision.
        */
        static public BigComplex scalePrec(final BigComplex x, int d)
        {
                return new BigComplex( scalePrec(x.re,d),scalePrec(x.im,d)) ;
        }

        /** Boost the precision by appending decimal zeros to the value. This returns a value which appears to have
        * a higher precision than the input.
        * @param x The input value
        * @param mc The requirement on the minimum precision on return.
        * @return The same value as the input but with increased (pseudo) precision.
        */
        static public BigDecimal scalePrec(final BigDecimal x, final MathContext mc)
        {
                final int diffPr = mc.getPrecision() - x.precision() ;
                if ( diffPr > 0 )
                        return scalePrec(x, diffPr) ;
                else
                        return x ;
        } /* BigDecimalMath.scalePrec */

        /** Convert an absolute error to a precision.
        * @param x The value of the variable
        * @param xerr The absolute error in the variable
        * @return The number of valid digits in x.
        *    The value is rounded down, and on the pessimistic side for that reason.
        * @since 2009-06-25
        */
        static public int err2prec(BigDecimal x, BigDecimal xerr)
        {
                return err2prec( xerr.divide(x,MathContext.DECIMAL64).doubleValue() );
        }

        /** Convert an absolute error to a precision.
        * @param x The value of the variable
        *    The value returned depends only on the absolute value, not on the sign.
        * @param xerr The absolute error in the variable
        *    The value returned depends only on the absolute value, not on the sign.
        * @return The number of valid digits in x.
        *    Derived from the representation x+- xerr, as if the error was represented
        *    in a "half width" (half of the error bar) form.
        *    The value is rounded down, and on the pessimistic side for that reason.
        * @since 2009-05-30
        */
        static public int err2prec(double x, double xerr)
        {
                /* Example: an error of xerr=+-0.5 at x=100 represents 100+-0.5 with
                * a precision = 3 (digits).
                */
                return 1+(int)(Math.log10(Math.abs(0.5*x/xerr) ) );
        }

        /** Convert a relative error to a precision.
        * @param xerr The relative error in the variable.
        *    The value returned depends only on the absolute value, not on the sign.
        * @return The number of valid digits in x.
        *    The value is rounded down, and on the pessimistic side for that reason.
        * @since 2009-08-05
        */
        static public int err2prec(double xerr)
        {
                /* Example: an error of xerr=+-0.5 a precision of 1 (digit), an error of
                * +-0.05 a precision of 2 (digits)
                */
                return 1+(int)(Math.log10(Math.abs(0.5/xerr) ) );
        }

        /** Convert a precision (relative error) to an absolute error.
        *    The is the inverse functionality of err2prec().
        * @param x The value of the variable
        *    The value returned depends only on the absolute value, not on the sign.
        * @param prec The number of valid digits of the variable.
        * @return the absolute error in x.
        *    Derived from the an accuracy of one half of the ulp.
        * @since 2009-08-09
        */
        static public double prec2err(final double x, final int prec)
        {
                return 5.*Math.abs(x)*Math.pow(10.,-prec) ;
        }

} /* BigDecimalMath */
