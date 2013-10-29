package edu.uoregon.nic.nemo.portal

class SecUser implements Comparable<SecUser>{

	transient springSecurityService

	static constraints = {
		username blank: false, unique: true,nullable: false
		password blank: false,nullable: false
//        laboratory nullable:  true
	}

    static belongsTo = [
            Laboratory
    ]

    static hasMany = [
            laboratories: Laboratory
            ,publications: Publication
    ]

	static mapping = {
		password column: '`password`'
	}


    String username
    String password
    boolean enabled
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    // my added
//    Laboratory laboratory
    String fullName

	Set<Role> getAuthorities() {
		SecUserRole.findAllBySecUser(this).collect { it.role } as Set
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService ? springSecurityService.encodePassword(password) : password
	}

    def getEmail(){
        return username
    }

    @Override
    int compareTo(SecUser t) {
        if (t.fullName && fullName){
            return fullName.compareTo(t.fullName)
        }

        return username.compareTo(t.username)
    }
}
