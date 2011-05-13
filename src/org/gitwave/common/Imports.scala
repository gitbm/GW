package org.gitwave.common

object Imports {
//	def neu[T](implicit f : Factory[T]) : T = f.create
//	def neu[T](id : String)(implicit f : Factory[T]) : T = f.createWithId(id)
//
//	
//	// Explore using an Any in Factory class and then a cast in Factory1 class !
//	def neu2[T, C](arg : C)(implicit f : Factory1[T, C]) : T = f.create(arg)
//	def neu3[T, C](id : String, arg : C)(implicit f : Factory1[T, C]) : T = f.createWithId(id, arg)
	
	// Use this method to create an object with no arguments 
	//  eg val x = neu[MyType]
	// Assumes an implicit Factory has been defined in scope for the type 
	//   eg implicit val imp : Factory[MyType] = FactoryDefault(classOf[MyTypeConcrete])	 
	def neu[T](implicit f : Factory[T]) : T = f.create

	// For future use to allow pre-created objects (eg Spring like configuration) to be referenced by ID or other means
	def neu[T](id : String)(implicit f : Factory[T]) : T = f.createWithId(id)
	def neu[T](meta : AnyRef)(implicit f : Factory[T]) : T = f.createWithAnyRef(meta)

	// The following methods are for creating an object with a single constructor argument
	
	// The immediately following method is for explicitly stating the type constructor argument
	//   eg val x = neu1[MyType, Int](5)
	// Assumes an implicit Factory has been defined in scope for the type 
	//   eg implicit val imp : Factory1[MyType, Int] = Factory1Default(classOf[MyTypeConcrete], classOf[Int])		 
	def neu1[T, C](arg : C)(implicit f : Factory1[T, C]) : T = f.create(arg)
	
	// The following method is for implicitly working out the constructor argument type
	// Scala does not seem to allow specifying a single Type and getting the compiler to work out what the other one is
	// It seems it is none or all so needed an extra helper class to get this to work.  This in turn meant it became
	// incompatible with the no argument case (since we need the argument to be present to cause the apply method to be called)
	// so method needed to be a different name ie neu1 rather than neu
	// Someone cleverer or with more Scala knowledge may be able to come up with a way to get all variations to use the same
	// method name but it eluded me.
	//   eg val x = neu1[MyType](5)
	// Makes use of same implicit factory as the explicit variation
	def neu1[T] = new Factory1Helper[T]

	// The following methods are for creating an object with 2 constructor arguments
	
	// The immediately following method is for explicitly stating the type constructor arguments
	//   eg val x = neu2[MyType, Int, String](5, "foo")
	// Assumes an implicit Factory has been defined in scope for the type 
	//   eg implicit val imp : Factory2[MyType, Int, String] = Factory2Default(classOf[MyTypeConcrete], classOf[Int], classOf[String])		 
	def neu2[T, C1, C2](arg1 : C1, arg2 : C2)(implicit f : Factory2[T, C1, C2]) : T = f.create(arg1, arg2)

	// The following method is for implicitly working out the constructor argument types
	//   eg val x = neu2[MyType](5, "foo")
	// Makes use of same implicit factory as the explicit variation
	def neu2[T] = new Factory2Helper[T]
	
	var debug = true;
	def dbg(f : => Unit) { 	if (debug) { f } }
	def dbprintln(s : String) { dbg { println(s) } 	}

}
