package apacheignite.wok;




import java.util.List;

import javax.cache.Cache;

import org.apache.ignite.springdata22.repository.IgniteRepository;
import org.apache.ignite.springdata22.repository.config.Query;
import org.apache.ignite.springdata22.repository.config.RepositoryConfig;





@RepositoryConfig(cacheName = "PersonCache")
public interface PersonRepository extends IgniteRepository<Person, Integer> {

	
	//SELECT queries never read through data from the external database. 
	//To execute select queries, the data must be pre-loaded from the database into 
	//the cache by calling the loadCache() method.
	
	public List<Person> findByName(String name);
	
	@Query("select * from Person")
	public List<Person> findAll();
	
	
	/**
     * Gets all the persons with the given name.
     * @param name Person name.
     * @return A list of Persons with the given first name.
     */


    /**
     * Returns top Person with the specified surname.
     * @param name Person surname.
     * @return Person that satisfy the query.
     */
    //public Cache.Entry<Long, Person> findTopByLastNameLike(String name);



}
