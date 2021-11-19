package com.yadavan88.example
import enumeratum._

sealed trait Country extends EnumEntry
object Country extends Enum[Country] {
  case object Germany extends Country
  case object India extends Country

  override val values: IndexedSeq[Country] = findValues
}

object Test extends App {

  val countries = Country.values
  assert(countries.size == 2)
  assert(countries.equals(Seq(Country.Germany, Country.India)))

  val india = Country.withName("India")
  println(india)

  val capital = india match {
    case Country.India => println("New Delhi")
  }

  val usa: Option[Country] = Country.withNameOption("USA")
  println(usa)

  val germanyCaps = Country.withNameInsensitive("GERMANY")
  assert(germanyCaps == Country.Germany)
  val indiaMixed = Country.withNameInsensitive("iNDia")
  assert(indiaMixed == Country.India)
  val usaInsensitive = Country.withNameInsensitiveOption("uSA")
  assert(usaInsensitive.isEmpty)
  val germanyIndex = Country.indexOf(Country.Germany)
  assert(germanyIndex == 0)
}

sealed abstract class Gender(override val entryName: String) extends EnumEntry
object Gender extends Enum[Gender]{
  override def values: IndexedSeq[Gender] = findValues
  case object Male extends Gender("M")
  case object Female extends Gender("F")
  case object Other extends Gender("O")
}

object Again extends App {

  val male = Gender.withName("M")
  assert(male == Gender.Male)

}

sealed trait Continent extends EnumEntry with EnumEntry.UpperWords
object Continent extends Enum[Continent] {
  override val values: IndexedSeq[Continent] = findValues
  case object Asia extends Continent
  case object Africa extends Continent
  case object Europe extends Continent
  case object NorthAmerica extends Continent
  case object SouthAmerica extends Continent
  case object Australia extends Continent
  case object Antartica extends Continent
}

object ContinentApp extends App {

  val continents = Continent.values
  println(continents)
  val asiaCap = Continent.withName("ASIA")
  println(asiaCap)  
  val asia = Continent.withName("Asia")
  println(asia)
}

sealed trait NamingConvention extends EnumEntry with EnumEntry.LowerCamelcase
object NamingConvention extends Enum[NamingConvention] {
  override val values: IndexedSeq[NamingConvention] = findValues
  case object JavaStyle extends NamingConvention
  case object ScalaStyle extends NamingConvention
  case object PythonStyle extends NamingConvention with EnumEntry.Snakecase
}

object NamingConventionApp extends App {

  val conventions = NamingConvention.values
  val java = NamingConvention.withName("avaStyle")
  println(java)

}

import enumeratum.values._
sealed abstract class HttpCode(val value:Int, val name:String) extends IntEnumEntry
object HttpCode extends IntEnum[HttpCode] {
  override def values: IndexedSeq[HttpCode] = findValues
  case object OK extends HttpCode(200, "Ok")
  case object BadRequest extends HttpCode(400, "Bad Request")
}

object HttpCodeApp extends App {

  val bad = HttpCode.withValue(400)
  assert(bad == HttpCode.BadRequest)
  assert(bad.name == "Bad Request")
}