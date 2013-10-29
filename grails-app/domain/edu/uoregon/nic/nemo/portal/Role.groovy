package edu.uoregon.nic.nemo.portal

class Role {

    public static ROLE_ADMIN = "ROLE_ADMIN"
    public static ROLE_VERIFIED = "ROLE_VERIFIED"
    public static ROLE_UNVERIFIED = "ROLE_UNVERIFIED"

	String authority

	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
	}
}
