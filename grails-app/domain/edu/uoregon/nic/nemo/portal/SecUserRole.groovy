package edu.uoregon.nic.nemo.portal

import org.apache.commons.lang.builder.HashCodeBuilder

class SecUserRole implements Serializable {

	SecUser secUser
	Role role

	boolean equals(other) {
		if (!(other instanceof SecUserRole)) {
			return false
		}

		other.secUser?.id == secUser?.id &&
			other.role?.id == role?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (secUser) builder.append(secUser.id)
		if (role) builder.append(role.id)
		builder.toHashCode()
	}

	static SecUserRole get(long secUserId, long roleId) {
		find 'from SecUserRole where secUser.id=:secUserId and role.id=:roleId',
			[secUserId: secUserId, roleId: roleId]
	}

	static SecUserRole create(SecUser secUser, Role role, boolean flush = false) {
		new SecUserRole(secUser: secUser, role: role).save(flush: flush, insert: true)
	}

	static boolean remove(SecUser secUser, Role role, boolean flush = false) {
		SecUserRole instance = SecUserRole.findBySecUserAndRole(secUser, role)
		if (!instance) {
			return false
		}

		instance.delete(flush: flush)
		true
	}

	static void removeAll(SecUser secUser) {
		executeUpdate 'DELETE FROM SecUserRole WHERE secUser=:secUser', [secUser: secUser]
	}

	static void removeAll(Role role) {
		executeUpdate 'DELETE FROM SecUserRole WHERE role=:role', [role: role]
	}

	static mapping = {
		id composite: ['role', 'secUser']
		version false
	}
}
