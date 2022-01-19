package apacheignite.wok;

import java.util.List;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
public class IgniteController {
	

	
	@Autowired
	PersonRepository repo;

	
	
	@GetMapping("/save")
	public Person save(@RequestBody Person person) {
		return repo.save(person.getId(), person);
	}
	
	@GetMapping("/{name}")
	public List<Person> getByName(@PathVariable("name") String name) {
		return repo.findByName(name);
	}
	
	@GetMapping("/all")
	public List<Person> finAll() {
		IgniteCache<Integer, Person> personCache = Ignition.ignite().cache("PersonCache");
		personCache.loadCache(null);
		return repo.findAll();
	}



}
