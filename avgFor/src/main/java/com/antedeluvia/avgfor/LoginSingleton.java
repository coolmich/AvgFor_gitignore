package com.antedeluvia.avgfor;

public class LoginSingleton {
	  private static LoginSingleton instance;
	   
	  private String userID;
	 
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
	  
	  public void setUID(String id) {
		  userID = id;
	  }
}
