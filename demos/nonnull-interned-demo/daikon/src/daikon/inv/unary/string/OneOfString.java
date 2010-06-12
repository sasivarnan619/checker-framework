// ***** This file is automatically generated from OneOf.java.jpp

package daikon.inv.unary.string;

import daikon.*;
import daikon.inv.*;

import utilMDE.*;

import java.io.*;
import java.util.logging.Logger;
import java.util.logging.Level;

  import java.util.regex.*;

import java.util.*;

// This subsumes an "exact" invariant that says the value is always exactly
// a specific value.  Do I want to make that a separate invariant
// nonetheless?  Probably not, as this will simplify implication and such.

  /**
   * Represents String variables that take on only a few distinct
   * values. Prints as either
   * <samp>x == c</samp> (when there is only one value)
   * or as <samp>x one of {c1, c2, c3}</samp> (when there are multiple values).
   */

public final class OneOfString
  extends SingleString
  implements OneOf
{
  // We are Serializable, so we specify a version to allow changes to
  // method signatures without breaking serialization.  If you add or
  // remove fields, you should change this number to the current date.
  static final long serialVersionUID = 20030822L;

  /**
   * Debugging logger.
   **/
  public static final Logger debug
    = Logger.getLogger (OneOfString.class.getName());

  // Variables starting with dkconfig_ should only be set via the
  // daikon.config.Configuration interface.
  /**
   * Boolean.  True iff OneOf invariants should be considered.
   **/
  public static boolean dkconfig_enabled = true;

  /**
   * Positive integer.  Specifies the maximum set size for this type
   * of invariant (x is one of <code>size</code> items).
   **/

  public static int dkconfig_size = 3;

  // Probably needs to keep its own list of the values, and number of each seen.
  // (That depends on the slice; maybe not until the slice is cleared out.
  // But so few values is cheap, so this is quite fine for now and long-term.)

  private String[] elts;
  private int num_elts;

  public OneOfString (PptSlice slice) {
    super (slice);
    if (slice == null)
      return;

    elts = new String[dkconfig_size];

    num_elts = 0;
  }

  private static OneOfString proto;

  /** Returns the prototype invariant for OneOfString **/
  public static Invariant get_proto() {
    if (proto == null)
      proto = new OneOfString (null);
    return (proto);
  }

  /** returns whether or not this invariant is enabled **/
  public boolean enabled() {
    return dkconfig_enabled;
  }

  /** instantiate an invariant on the specified slice **/
  public Invariant instantiate_dyn (PptSlice slice) {
    return new OneOfString(slice);
  }

  public boolean is_boolean() {
    return (var().file_rep_type.elementType() == ProglangType.BOOLEAN);
  }
  public boolean is_hashcode() {
    return (var().file_rep_type.elementType() == ProglangType.HASHCODE);
  }

  public OneOfString clone() {
    OneOfString result = (OneOfString) super.clone();
    result.elts = elts.clone();

    result.num_elts = this.num_elts;
    return result;
  }

  public int num_elts() {
    return num_elts;
  }

  public Object elt() {
    return elt(0);
  }

  public Object elt(int index) {
    if (num_elts <= index)
      throw new Error("Represents " + num_elts + " elements, index " + index + " not valid");

    return elts[index];
  }

  static Comparator<String> comparator = new UtilMDE.NullableStringComparator();

  private void sort_rep() {
    Arrays.sort(elts, 0, num_elts , comparator);
  }

  public String min_elt() {
    if (num_elts == 0)
      throw new Error("Represents no elements");
    sort_rep();
    return elts[0];
  }

  public String max_elt() {
    if (num_elts == 0)
      throw new Error("Represents no elements");
    sort_rep();
    return elts[num_elts-1];
  }

  // Assumes the other array is already sorted
  public boolean compare_rep(int num_other_elts, String[] other_elts) {
    if (num_elts != num_other_elts)
      return false;
    sort_rep();
    for (int i=0; i < num_elts; i++)
      if (! ((elts[i]) == (other_elts[i]))) // elements are interned
        return false;
    return true;
  }

  private String subarray_rep() {
    // Not so efficient an implementation, but simple;
    // and how often will we need to print this anyway?
    sort_rep();
    StringBuffer sb = new StringBuffer();
    sb.append("{ ");
    for (int i=0; i<num_elts; i++) {
      if (i != 0)
        sb.append(", ");

      if (PrintInvariants.dkconfig_static_const_infer) {
        boolean curVarMatch = false;
        PptTopLevel pptt = ppt.parent;
        for (VarInfo vi : pptt.var_infos) {
          if (vi.is_static_constant && VarComparability.comparable(vi, var())) {
            Object constantVal = vi.constantValue();
            if (constantVal.equals(elts[i])) {
              sb.append(vi.name());
              curVarMatch = true;
            }
          }
        }

        if (curVarMatch == false) {
          sb.append(((elts[i]==null) ? "null" : "\"" + UtilMDE.escapeNonASCII(elts[i]) + "\""));
        }
      }
      else {
        sb.append(((elts[i]==null) ? "null" : "\"" + UtilMDE.escapeNonASCII(elts[i]) + "\""));
      }

    }
    sb.append(" }");
    return sb.toString();
  }

  public String repr() {
    return "OneOfString" + varNames() + ": "
      + "falsified=" + falsified
      + ", num_elts=" + num_elts
      + ", elts=" + subarray_rep();
  }

  public String[] getElts() {
    String[] temp = new String[elts.length];
    for (int i=0; i < elts.length; i++) {
      temp[i] = elts[i];
    }
    return temp;
  }

  public String format_using(OutputFormat format) {
    sort_rep();

    if (format.isJavaFamily()) return format_java_family(format);

    if (format == OutputFormat.DAIKON) {
      return format_daikon();
    } else if (format == OutputFormat.IOA) {
      return format_ioa();
    } else if (format == OutputFormat.SIMPLIFY) {
      return format_simplify();
    } else if (format == OutputFormat.ESCJAVA) {
      return format_esc();
    } else {
      return format_unimplemented(format);
    }
  }

  public String format_daikon() {
    String varname = var().name();
    if (num_elts == 1) {

        boolean is_type = is_type();
        if (! is_type) {
          return varname + " == " + ((elts[0]==null) ? "null" : "\"" + UtilMDE.escapeNonASCII(elts[0]) + "\"");
        } else {
          // It's a type
          String str = elts[0];
          if ((str == null) || "null".equals(str)) {
            return varname + " == null";
          } else {
            if (str.startsWith("[")) {
              str = UtilMDE.classnameFromJvm(str);
            }
            if (PrintInvariants.dkconfig_static_const_infer) {
              PptTopLevel pptt = ppt.parent;
              for (VarInfo vi : pptt.var_infos) {
                if (vi.is_static_constant && VarComparability.comparable(vi, var())) {
                  Object constantVal = vi.constantValue();
                  if (constantVal.equals(str)) {
                    return varname + " == " + vi.name();
                  }
                }
              }
            }
            // ".class" (which is a suffix for a type name) and not
            // getClassSuffix (which is a suffix for an expression).
            return varname + " == " + str + ".class";
          }
        }

    } else {
      return varname + " one of " + subarray_rep();
    }
  }

  private boolean is_type() {
    return var().has_typeof();
  }

  /* IOA */
  public String format_ioa() {
    sort_rep();

    String varname = var().ioa_name();

    String result;

    result = "";
    for (int i=0; i<num_elts; i++) {
      if (i != 0) { result += " \\/ ("; }
      if (PrintInvariants.dkconfig_static_const_infer) {
        boolean curVarMatch = false;
        PptTopLevel pptt = ppt.parent;
        for (VarInfo vi : pptt.var_infos) {
          if (vi.is_static_constant && VarComparability.comparable(vi, var())) {
            Object constantVal = vi.constantValue();
            if (constantVal.equals(elts[i])) {
              result += varname + " = " + vi.name() + ")";
              curVarMatch = true;
            }
          }
        }
        if (curVarMatch == false) {
          result += varname + " = " + ((elts[i]==null) ? "null" : "\"" + UtilMDE.escapeNonASCII(elts[i]) + "\"") + ")";
        }
      }
      else {
        result += varname + " = " + ((elts[i]==null) ? "null" : "\"" + UtilMDE.escapeNonASCII(elts[i]) + "\"") + ")";
      }
    }
    result += ")";

    return result;
  }

  private static Pattern dollar_char_pat = Pattern.compile("\\$([A-Za-z])");

  private static String format_esc_string2type(String str) {
    if ((str == null) || "null".equals(str)) {
      return "\\typeof(null)";
    }
    String type_str;
    if (str.startsWith("[")) {
      type_str = UtilMDE.classnameFromJvm(str);
    } else {
      type_str = str;
      if (type_str.startsWith("\"") && type_str.endsWith("\"")) {
        type_str = type_str.substring(1, type_str.length()-1);
      }
    }

    // Inner classes
    // type_str = type_str.replace('$', '.');
    // For named inner classes, convert "$" to ".".
    // For anonymous inner classes, leave as "$".
    Matcher m = dollar_char_pat.matcher(type_str);
    type_str = m.replaceAll(".$1");

    return "\\type(" + type_str + ")";
  }

  public String format_esc() {
    sort_rep();

    String varname = var().esc_name();

    String result;

    // We cannot say anything about Strings in ESC, just types (which
    // Daikon stores as Strings).
    if (! is_type()) {
      result = format_unimplemented(OutputFormat.ESCJAVA); // "needs to be implemented"
    } else {
      // Format   \typeof(theArray) = "[Ljava.lang.Object;"
      //   as     \typeof(theArray) == \type(java.lang.Object[])
      // ... but still ...
      // format   \typeof(other) = "package.SomeClass;"
      //   as     \typeof(other) == \type(package.SomeClass)

      result = "";
      for (int i=0; i<num_elts; i++) {
        if (i != 0) { result += " || "; }
        result += varname + " == " + format_esc_string2type(elts[i]);
      }
    }

    return result;
  }

  public String format_java_family(OutputFormat format) {

    String result;

    // Setting up the name of the unary variable
    String varname = var().name_using(format);

    result = "";
    boolean is_type = is_type();
    for (int i=0; i<num_elts; i++) {
      if (i != 0) { result += " || "; }
      String str = elts[i];
      if (!is_type) {
        result += varname + ".equals(" + ((str==null) ? "null" : "\"" + UtilMDE.escapeNonASCII(str) + "\"") + ")";
      } else {
        // It's a type
        if ((str == null) || "null".equals(str)) {
          result += varname + " == null";
        } else {
          if (str.startsWith("[")) {
            str = UtilMDE.classnameFromJvm(str);
          }
          // ".class" (which is a suffix for a type name) and not
          // getClassSuffix (which is a suffix for an expression).
          result += varname + " == " + str + ".class";
        }
      }
    }

    return result;
  }

  public String format_simplify() {

    sort_rep();

    String varname =
      var().simplifyFixup(var().name_using(OutputFormat.SIMPLIFY));

    String result;

    result = "";
    boolean is_type = is_type();
    for (int i=0; i<num_elts; i++) {
      String value = elts[i];
      if (is_type) {
        if (value == null) {
          // do nothing
        } else if (value.startsWith("[")) {
          value = UtilMDE.classnameFromJvm(value);
        } else if (value.startsWith("\"") && value.endsWith("\"")) {
          value = value.substring(1, value.length()-1);
        }
        value = "|T_" + value + "|";
      } else {
        value = simplify_format_string(value);
      }
      result += " (EQ " + varname + " " + value + ")";
    }
    if (num_elts > 1) {
      result = "(OR" + result + ")";
    } else if (num_elts == 1) {
      // chop leading space
      result = result.substring(1);
    } else if (num_elts == 0) {
      return format_too_few_samples(OutputFormat.SIMPLIFY, null);
    }

    if (result.indexOf("format_simplify") == -1)
      daikon.simplify.SimpUtil.assert_well_formed(result);
    return result;
  }

  /* Old version with interleaved "ifdefs", replaced with following version
     using separate ELT and non-elt routines with a shared subroutine

  public InvariantStatus add_modified(String v, int count) {
    InvariantStatus status = InvariantStatus.NO_CHANGE;

    Assert.assertTrue(Intern.isInterned(v));

    for (int i=0; i<num_elts; i++) {
      //if (logDetail())
      //  log ("add_modified (" + v + ")");
      if (((elts[i]) == ( v))) {

        return InvariantStatus.NO_CHANGE;
      }
    }
    if (num_elts == dkconfig_size) {
      if (logOn() || debug.isLoggable(Level.FINE))
        log (debug, "destroy of '" + format() + "' add_modified (" + v + ")");
      destroyAndFlow();
      return;
    }

    if (is_type() && (num_elts == 1)) {
      destroyAndFlow();
      return;
    }

    // We are significantly changing our state (not just zeroing in on
    // a constant), so we have to flow a copy before we do so.  We even
    // need to clone if this has 0 elements becuase otherwise, lower
    // ppts will get versions of this with multiple elements once this is
    // expanded.
    cloneAndFlow();

    elts[num_elts] = v;
    num_elts++;
    status = InvariantStatus.WEAKENED;

  return status;
  }

  */

  public InvariantStatus add_modified(String a, int count) {
    return runValue(a, count, true);
  }

  public InvariantStatus check_modified(String a, int count) {
    return runValue(a, count, false);
  }

  private InvariantStatus runValue(String v, int count, boolean mutate) {
    InvariantStatus status;
    if (mutate) {
      status = add_mod_elem(v, count);
    } else {
      status = check_mod_elem(v, count);
    }
    if (status == InvariantStatus.FALSIFIED) {
      if (logOn() && mutate) {
        StringBuffer eltString = new StringBuffer();
        for (int i = 0; i < num_elts; i++) {
          eltString.append(((elts[i]==null) ? "null" : "\"" + UtilMDE.escapeNonASCII(elts[i]) + "\"") + " ");
        }
        log ("destroyed on sample " + ((v==null) ? "null" : "\"" + UtilMDE.escapeNonASCII(v) + "\"") + " previous vals = { "
             + eltString + "} num_elts = " + num_elts);
      }
      return InvariantStatus.FALSIFIED;
    }
    return status;
  }

  /**
   * Adds a single sample to the invariant.  Returns
   * the appropriate InvariantStatus from the result
   * of adding the sample to this.
   */
  public InvariantStatus add_mod_elem (String v, int count) {
    InvariantStatus status = check_mod_elem(v, count);
    if (status == InvariantStatus.WEAKENED) {
      elts[num_elts] = v;
      num_elts++;
    }
    return status;
  }

  /**
   * Checks a single sample to the invariant.  Returns
   * the appropriate InvariantStatus from the result
   * of adding the sample to this.
   */
  public InvariantStatus check_mod_elem (String v, int count) {

    // Look for v in our list of previously seen values.  If it's
    // found, we're all set.
    for (int i=0; i<num_elts; i++) {
      //if (logDetail())
      //  log ("add_modified (" + v + ")");
      if (((elts[i]) == ( v))) {
        return (InvariantStatus.NO_CHANGE);
      }
    }

    if (num_elts == dkconfig_size) {
      return (InvariantStatus.FALSIFIED);
    }

    if (is_type() && (num_elts == 1)) {
      return (InvariantStatus.FALSIFIED);
    }

    return (InvariantStatus.WEAKENED);
  }

  protected double computeConfidence() {
    // This is not ideal.
    if (num_elts == 0) {
      return Invariant.CONFIDENCE_UNJUSTIFIED;
    } else {
      return Invariant.CONFIDENCE_JUSTIFIED;
    }
  }

  public DiscardInfo isObviousStatically(VarInfo[] vis) {
    // Static constants are necessarily OneOf precisely one value.
    // This removes static constants from the output, which might not be
    // desirable if the user doesn't know their actual value.
    if (vis[0].isStaticConstant()) {
      Assert.assertTrue(num_elts <= 1);
      return new DiscardInfo(this, DiscardCode.obvious, vis[0].name() + " is a static constant.");
    }
    return super.isObviousStatically(vis);
  }

  /**
   * Oneof can merge different formulas from lower points to create a single
   * formula at an upper point.
   */
  public boolean mergeFormulasOk() {
    return (true);
  }

  public boolean isSameFormula(Invariant o) {
    OneOfString other = (OneOfString) o;
    if (num_elts != other.num_elts)
      return false;
    if (num_elts == 0 && other.num_elts == 0)
      return true;

    sort_rep();
    other.sort_rep();

    for (int i=0; i < num_elts; i++) {
      if (! ((elts[i]) == (other.elts[i])))
        return false;
    }

    return true;
  }

  public boolean isExclusiveFormula(Invariant o) {
    if (o instanceof OneOfString) {
      OneOfString other = (OneOfString) o;

      if (num_elts == 0 || other.num_elts == 0)
        return false;
      for (int i=0; i < num_elts; i++) {
        for (int j=0; j < other.num_elts; j++) {
          if (((elts[i]) == (other.elts[j]))) // elements are interned
            return false;
        }
      }

      return true;
    }

    return false;
  }

  // OneOf invariants that indicate a small set of possible values are
  // uninteresting.  OneOf invariants that indicate exactly one value
  // are interesting.
  public boolean isInteresting() {
    if (num_elts() > 1) {
      return false;
    } else {
      return true;
    }
  }

  public boolean hasUninterestingConstant() {

    return false;
  }

  public boolean isExact() {
    return (num_elts == 1);
  }

  // Look up a previously instantiated invariant.
  public static OneOfString find(PptSlice ppt) {
    Assert.assertTrue(ppt.arity() == 1);
    for (Invariant inv : ppt.invs) {
      if (inv instanceof OneOfString)
        return (OneOfString) inv;
    }
    return null;
  }

  // Interning is lost when an object is serialized and deserialized.
  // Manually re-intern any interned fields upon deserialization.
  private void readObject(ObjectInputStream in) throws IOException,
    ClassNotFoundException {
    in.defaultReadObject();

    for (int i=0; i < num_elts; i++)
      elts[i] = Intern.intern(elts[i]);
  }

  /**
   * Merge the invariants in invs to form a new invariant.  Each must be
   * a OneOfString invariant.  This code finds all of the oneof values
   * from each of the invariants and returns the merged invariant (if any).
   *
   * @param invs       List of invariants to merge.  The invariants must all be
   *                   of the same type and should come from the children of
   *                   parent_ppt.  They should also all be permuted to match
   *                   the variable order in parent_ppt.
   * @param parent_ppt Slice that will contain the new invariant
   */
  public Invariant merge (List<Invariant> invs, PptSlice parent_ppt) {

    // Create the initial parent invariant from the first child
    OneOfString  first = (OneOfString) invs.get(0);
    OneOfString result = first.clone();
    result.ppt = parent_ppt;

    // Loop through the rest of the child invariants
    for (int i = 1; i < invs.size(); i++ ) {

      // Get this invariant
      OneOfString inv = (OneOfString) invs.get (i);

      // Loop through each distinct value found in this child and add
      // it to the parent.  If the invariant is falsified, there is no parent
      // invariant
      for (int j = 0; j < inv.num_elts; j++) {
        String val = inv.elts[j];

        InvariantStatus status = result.add_mod_elem(val, 1);
        if (status == InvariantStatus.FALSIFIED) {
          result.log ("child value '" + val + "' destroyed oneof");
          return (null);
        }
      }
    }

    result.log ("Merged '" + result.format() + "' from " + invs.size()
                + " child invariants");
    return (result);
  }

  /**
   * Setup the invariant with the specified elements.  Normally
   * used when searching for a specified OneOf
   */
  public void set_one_of_val (String[] vals) {

    num_elts = vals.length;
    for (int i = 0; i < num_elts; i++)
      elts[i] = Intern.intern (vals[i]);
  }

  /**
   * Returns true if every element in this invariant is contained in
   * the specified state.  For example if x = 1 and the state contains
   * 1 and 2, true will be returned.
   */
  public boolean state_match (Object state) {

    if (num_elts == 0)
      return (false);

    if (!(state instanceof String[]))
      System.out.println ("state is of class '" + state.getClass().getName()
                          + "'");
    String[] e = (String[]) state;
    for (int i = 0; i < num_elts; i++) {
      boolean match = false;
      for (int j = 0; j < e.length; j++) {
        if (elts[i] == e[j]) {
          match = true;
          break;
        }
      }
      if (!match)
        return (false);
    }
    return (true);
  }

}
