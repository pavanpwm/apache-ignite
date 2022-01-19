package apacheignite.wok;


import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import javax.cache.configuration.Factory;
import javax.sql.DataSource;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStoreFactory;
import org.apache.ignite.cache.store.jdbc.JdbcType;
import org.apache.ignite.cache.store.jdbc.JdbcTypeField;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.gridgain.control.agent.configuration.ControlCenterAgentConfiguration;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import com.mysql.cj.jdbc.MysqlDataSource;

import ch.qos.logback.core.db.dialect.PostgreSQLDialect;


@Configuration
@org.apache.ignite.springdata22.repository.config.EnableIgniteRepositories  //add basepackages if repos are in different packages
public class SpringDataConfig {

    @Bean
    public Ignite igniteInstance() {
    	IgniteConfiguration igniteCfg = new IgniteConfiguration();
    	
//    	igniteCfg.setClientMode(true);
//    	also set peerclassloading
//    	TcpDiscoverySpi spi = new TcpDiscoverySpi();
//    	TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
//    	// Set initial IP addresses.
//    	// Note that you can optionally specify a port or a port range.
//    	ipFinder.setAddresses(Arrays.asList("127.0.0.1:47500..47509"));
//    	spi.setIpFinder(ipFinder);
//    	igniteCfg.setDiscoverySpi(spi);
    	

    	CacheConfiguration<Integer, Person> personCacheCfg = new CacheConfiguration<>();
    	personCacheCfg.setName("PersonCache");
    	personCacheCfg.setCacheMode(CacheMode.PARTITIONED);
    	personCacheCfg.setAtomicityMode(CacheAtomicityMode.ATOMIC);
    	personCacheCfg.setReadThrough(true);
    	personCacheCfg.setWriteThrough(true);
    	
    	
    	CacheJdbcPojoStoreFactory<Integer, Person> factory = new CacheJdbcPojoStoreFactory<>();
    	factory.setDialect(new org.apache.ignite.cache.store.jdbc.dialect.BasicJdbcDialect());
    	factory.setDataSourceFactory((Factory<DataSource>)() -> {
    	    PGSimpleDataSource mysqlDataSrc = new PGSimpleDataSource();
    	    mysqlDataSrc.setURL("jdbc:postgresql://localhost:5432/postgres");
    	    mysqlDataSrc.setUser("postgres");
    	    mysqlDataSrc.setPassword("root");
    	    return mysqlDataSrc;
    	});

    	JdbcType personType = new JdbcType();
    	personType.setCacheName("PersonCache");
    	personType.setKeyType(Integer.class);
    	personType.setValueType(Person.class);
    	// Specify the schema if applicable
    	personType.setDatabaseSchema("public");
    	personType.setDatabaseTable("person");
    	personType.setKeyFields(new JdbcTypeField(java.sql.Types.INTEGER, "id", Integer.class, "id"));
    	personType.setValueFields(new JdbcTypeField(java.sql.Types.INTEGER, "id", Integer.class, "id"));
    	personType.setValueFields(new JdbcTypeField(java.sql.Types.VARCHAR, "name", String.class, "name"));
    	factory.setTypes(personType);
    	personCacheCfg.setCacheStoreFactory(factory);

    	QueryEntity qryEntity = new QueryEntity();
    	qryEntity.setKeyType(Integer.class.getName());
    	qryEntity.setValueType(Person.class.getName());
    	qryEntity.setKeyFieldName("id");
    	Set<String> keyFields = new HashSet<>();
    	keyFields.add("id");
    	qryEntity.setKeyFields(keyFields);

    	LinkedHashMap<String, String> fields = new LinkedHashMap<>();
    	fields.put("id", "java.lang.Integer");
    	fields.put("name", "java.lang.String");
    	qryEntity.setFields(fields);

    	personCacheCfg.setQueryEntities(Collections.singletonList(qryEntity));

    	igniteCfg.setCacheConfiguration(personCacheCfg);
        return Ignition.start(igniteCfg);
    }
}