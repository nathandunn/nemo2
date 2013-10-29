dataSource {
    pooled = true
//    driverClassName = "org.h2.Driver"
    driverClassName = "org.postgresql.Driver"
    username = "ndunn"
    password = ""
//    dialect = net.sf.hibernate.dialect.PostgreSQLDialect
    dialect = "org.hibernate.dialect.PostgreSQLDialect"
}


hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}
// environment specific settings
environments {
    development {
        dataSource {
//            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
//            url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
            url = "jdbc:postgresql://localhost:5432/nemo2_dev"
//            logSql = true
        }
    }
    staging {
        dataSource {
//            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
//            url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
            url = "jdbc:postgresql://localhost:5432/nemo2_staging"
//            logSql = true
        }
    }
    test {
        dataSource {
			dbCreate = "update"
//            dbCreate = "create-drop"
//            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
            url = "jdbc:postgresql://localhost:5432/nemo2_test"


        }
    }
    production {
        dataSource {
            dbCreate = "update"
//            url = "jdbc:h2:prodDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
//            url = "jdbc:postgresql://localhost:5432/nemo_dev"
            url = "jdbc:postgresql://localhost:5432/nemo2_production"
            pooled = true
            properties {
               maxActive = -1
               minEvictableIdleTimeMillis=1800000
               timeBetweenEvictionRunsMillis=1800000
               numTestsPerEvictionRun=3
               testOnBorrow=true
               testWhileIdle=true
               testOnReturn=true
               validationQuery="SELECT 1"
            }
        }
    }
}
