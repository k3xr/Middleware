package HolaMundoApp;


/**
* HolaMundoApp/HolaHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from holamundo.idl
* Wednesday, October 9, 2013 2:36:14 PM CEST
*/

abstract public class HolaHelper
{
  private static String  _id = "IDL:HolaMundoApp/Hola:1.0";

  public static void insert (org.omg.CORBA.Any a, HolaMundoApp.Hola that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static HolaMundoApp.Hola extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (HolaMundoApp.HolaHelper.id (), "Hola");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static HolaMundoApp.Hola read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_HolaStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, HolaMundoApp.Hola value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static HolaMundoApp.Hola narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof HolaMundoApp.Hola)
      return (HolaMundoApp.Hola)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      HolaMundoApp._HolaStub stub = new HolaMundoApp._HolaStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static HolaMundoApp.Hola unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof HolaMundoApp.Hola)
      return (HolaMundoApp.Hola)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      HolaMundoApp._HolaStub stub = new HolaMundoApp._HolaStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
