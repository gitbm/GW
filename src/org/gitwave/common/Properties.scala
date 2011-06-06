//package org.gitwave.common
//
//import org.acme.scalautil.Factory._
////import Implicits._
//
//trait Properties {
//	def initialise(initString : Option[String], defaults : Map[String, String] = Map()) : Properties
//	def addDefaults(defaults : Map[String, String]) : Properties
//	def get(name : String) : Option[String]
//	
//	def getOrElse(name : String, elseValue: String) = get(name).getOrElse(elseValue)
//	def getMandatory(name : String) = get(name).getOrElse(throw PropertyMissingException("Mandatory property (" + name + ") was not provided"))
//}
//
//case class PropertyMissingException(msg : String) extends RuntimeException(msg)
//
////object Properties extends Properties{
////	private var theProperties : Properties = _
////	private var init = false
////	
////	
////	def initialise(initString : Option[String], defaults : Map[String, String] = null) = initialiseImpl(initString, defaults, false) 
////	def initialiseOverwrite(initString : Option[String], defaults : Map[String, String] = null ) = initialiseImpl(initString, defaults, true) 
////
////	private def initialiseImpl(initString : Option[String], defaults : Map[String, String], overwrite : Boolean) = 
////		if (!init || overwrite) {
////			theProperties = create[Properties]
////			theProperties.initialise(initString, if (defaults == null) Map[String, String]() else defaults); 
////			init = true 
////		}
////	
////	def addDefaults(defaults : Map[String, String]) = theProperties.addDefaults(defaults)
////
////	def get(name : String) = { if (!init) { initialise(None) }; theProperties.get(name) }
////	def getOrElse(name : String, elseValue: String) = get(name).getOrElse(elseValue)
////	def getMandatory(name : String) = get(name).getOrElse(throw PropertyMissingException("Mandatory property (" + name + ") was not provided"))
////}