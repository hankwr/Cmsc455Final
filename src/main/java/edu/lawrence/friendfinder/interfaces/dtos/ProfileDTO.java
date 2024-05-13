package edu.lawrence.friendfinder.interfaces.dtos;
/**
 * This is a dto that the client will send to the server
 * Lacks certain fields that are computed in the backend (eg. ID)
 */

// Java-level incldues [Utility]
import java.util.List;

public class ProfileDTO{
	
	private String fullname;
	
	private String emailaddress;
	
	private int countrycode;
	
	private String phonenumber;
	
	private String bio;
	
	private List<String> tags;
	
	public ProfileDTO() {}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getEmailaddress() {
		return emailaddress;
	}

	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}

	public int getCountrycode() {
		return countrycode;
	}

	public void setCountrycode(int countrycode) {
		this.countrycode = countrycode;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
}