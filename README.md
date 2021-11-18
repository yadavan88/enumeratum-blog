## Enumeratum for a better enumeration in Scala

1. Introduction

Enumeration is a finite set of ordered values representing a domain or a collection. Enumerations or Enums are very widely used in all the programming languages to model the domain. Like other languages, Scala also supports enumerations. However, there are many practical problems with Scala's in-built enum type. In this tutorial, let's look at Enumeratum, a better enumeration library for Scala.

2. Existing Ways to Create Enums

There are mainly two ways in which we can create enumerations in Scala.

2.1. Using Enumeration Keyword

The in-built method to create enumerations in Scala is by extending with the abstract class Enumeration. However, there are some major issues with this approach:

Enumerations are erased at run-time.
No exhaustive check for pattern matching at compile time
2.2. Using Algebraic Data Type (ADT)

Due to the issues with Scala's Enumeration, enumerations are created as ADTs using sealed traits and case objects. This solved some of the issues. However, there are still some drawbacks with this approach:

No out-of-the-box solution to build and list enumerations
No automatic ordering for the items
Need to write custom logic for serialization and deserialization
3. Enumeratum

Enumeratum library tries to solve all the previously mentioned issues. Some of the advantages of using Enumeratum are:

Exhaustive pattern-matching warning
Very fast and easy to use
Many in-built utility methods to customize enumerations
Very good integration with popular JSON and Database libraries
3.1. Set-up

To use Enumeratum, we need to add the library-dependency:

libraryDependencies ++= Seq(
    "com.beachape" %% "enumeratum" % "1.7.0"
)

Now we can use the below import statement and the required classes will be available in scope:

import enumeratum._
3.2. Defining a Simple Enum

Let's look at how we can define a simple enum. We will be using the types EnumEntry and Enum to define our enumerations.

Firstly, we need to create a sealed trait or abstract class to define our enum type. For this, we need to extend the trait EnumEntry:

sealed trait Country extends EnumEntry

Now we can define the supported list of countries:

object Country extends Enum[Country] {
  case object Germany extends Country
  case object India extends Country
  override val values: IndexedSeq[Country] = findValues
}

Note that we have to implement the field values, with the list of possible values for the enum. Enumeratum already provides a method findValues which returns all the defined enum elements in the same order in which it is defined. This is done at compile time using macros and hence there is no run-time overhead too.

We can verify the above implementation:

val countries = Country.values
assert(countries.size == 2)
assert(countries.equals(Seq(Country.Germany, Country.India)))
3.3. Utility Methods

Enumeratum provides many utility methods on enum values. Let's look at some of the popular and useful ones.

To convert a String value to enum, we can use withName method:

Country.withName("India")

If the value is not a valid enum name, it will throw a NoSuchElementException at runtime. But to handle such cases, we can use withNameOption method:

val usa: Option[Country] = Country.withNameOption("USA")

It will return None if the value passed is an invalid enum name, otherwise, it will be wrapped in Some.

We can also handle case sensitiveness using different available utility methods:

val germanyCaps = Country.withNameInsensitive("GERMANY")
assert(germanyCaps == Country.Germany)
val indiaMixed = Country.withNameInsensitive("iNDia")
assert(indiaMixed == Country.India)
val usaInsensitive = Country.withNameInsensitiveOption("uSA")
assert(usaInsensitive.isEmpty)
val germanyIndex = Country.indexOf(Country.Germany)
assert(germanyIndex == 0)
3.5. Customizing Enum Values Using Overriding

By default, the enum value will be the same as the case object name. However, we can customize this name by overriding the entryName parameter:

sealed abstract class Gender(override val entryName: String) extends EnumEntry
object Gender extends Enum[Gender]{
  override def values: IndexedSeq[Gender] = findValues
  case object Male extends Gender("M")
  case object Female extends Gender("F")
  case object Other extends Gender("O")
}

Now, the enum values will be overwritten to M, F, O instead of Male, Female, and Other:

val male = Gender.withName("M")
assert(male == Gender.Male)
3.6. Customizing Enum Values Using Utility Traits

Instead of overriding the enum values explicitly, Enumeratum provides some utility traits for common scenarios. We can use these traits to extend our enum entries based on the requirement. These traits will internally override the entryName parameter with some of the common String conversions.  Let's look at an example:

sealed trait NamingConvention extends EnumEntry with EnumEntry.LowerCamelcase
object NamingConvention extends Enum[NamingConvention] {
  override val values: IndexedSeq[NamingConvention] = findValues
  case object JavaStyle extends NamingConvention
  case object ScalaStyle extends NamingConvention
  case object PythonStyle extends NamingConvention with EnumEntry.Snakecase
}

The trait NamingConvention is extending a trait EnumEntry.LowerCamelcase. The trait will modify the entryName parameter. This is equivalent to explicitly defining JavaStyle enum as:

sealed abstract class NamingConvention(override val entryName: String) extends EnumEntry 
object NamingConvention extends Enum[NamingConvention] {
  override val values: IndexedSeq[NamingConvention] = findValues
  case object JavaStyle extends NamingConvention("javaStyle")
  case object ScalaStyle extends NamingConvention("scalaStyle")
  case object PythonStyle extends NamingConvention("python_style")
}

Note that javaStyle and scalaStyle are using camel case format, whereas python_style is using snake case format. Apart from Snakecase and LowerCamelcase traits, some of the other implementations are Uppercase, Lowercase, CapitalHyphencase, and so on.

3.7. Defining Value Enum

Apart from the regular String-based enums, Enumeratum also supports enums for types like Int, Long, Char, and so on. Instead of extending with EnumEntry, we need to use the more specific type like IntEnumEntry, LongEnumEntry, etc:

import enumeratum.values._
sealed abstract class HttpCode(val value:Int, val name:String) extends IntEnumEntry
object HttpCode extends IntEnum[HttpCode] {
  override val values: IndexedSeq[HttpCode] = findValues
  case object OK extends HttpCode(200, "Ok")
  case object BadRequest extends HttpCode(400, "Bad Request")
}

Now, we can create the enum using the integer value:

val bad = HttpCode.withValue(400)
assert(bad == HttpCode.BadRequest)
assert(bad.name == "Bad Request")
4. Integration with Other Libraries

Enumeratum has very good integration with other libraries. It integrates well with JSON libraries as well as database libraries. Some of the supported libraries are Json4s, Play, Slick, ReactiveMongo, Circe, and many others. The full list of integrations is available here.

5. Enumeration in Scala 3

Prior to Scala-3, the enumeration in the standard library is far from perfect. However, in Scala-3, the enumerations are completely redesigned and all the major issues in the previous versions are solved. But, if it is not possible to upgrade to Scala-3, then Enumeratum is the best solution for managing enums.
