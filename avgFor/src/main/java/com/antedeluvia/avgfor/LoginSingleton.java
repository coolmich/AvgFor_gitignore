package com.antedeluvia.avgfor;

public class LoginSingleton {
	  private static LoginSingleton instance;
	   
	  private String userID;
      private String email;
      private String passwd;
	 
	  public static LoginSingleton getInstance()
	  {
	    // Return the instance
		  if (instance == null)
		    {
		      // Create the instance
		      instance = new LoginSingleton();
		    }
		  return instance;
	  }
	   
	  private LoginSingleton()
	  {
	    // Constructor hidden because this is a singleton
	  }
	   
	  public String getUID()
	  {
	    // Custom method
		  return userID;
	  }

      public String getEmail() { return email; }
      public String getPwd() { return passwd; }
	  
	  public void setUID(String id) {
		  userID = id;
	  }
      public void setEmail(String e) { email = e; }
      public void setPwd(String p) {passwd = p; }
      public void clear() { userID = ""; email = ""; passwd = ""; }
}
