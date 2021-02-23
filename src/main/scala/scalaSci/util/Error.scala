

package scalaSci.util

/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
/**
 * This trait is used to report errors showing the class and method within
 * which the error or flaw occured.
 */
trait Error
{
    /** Name of the class where the error occured
     */
    private val className = getClass.getSimpleName ()

    /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /**
     * Show the flaw by printing the error message.
     * @param method   the method where the error occured
     * @param message  the error message
     */
    def flaw (method: String, message: String)
    {
        println ("ERROR @ " + className + "." + method +  ": " + message)
    } // flaw

} // Error trait

