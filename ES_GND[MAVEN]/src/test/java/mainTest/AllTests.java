package mainTest;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map.Entry;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import elasticsearch.ElasticSearch;
import elasticsearch.ElasticSearchConnection;
import file.FileToLines;

public class AllTests {

	@SuppressWarnings({ "unused", "unchecked" })
	@Test
	public void JSONtoMapTest() {
		FileToLines ftl = new FileToLines( "2008-Feb-02-04.json" );

		HashMap<String, Object> jsonAsMap = null;
		HashMap<String, Object> source = null;
		double i = 0;
		for (String str : ftl.lines()) {
			i++;
			if (i == 10001)
				break;
			try {
				jsonAsMap = new ObjectMapper().readValue( str,
						new TypeReference<HashMap<String, Object>>() {} );
			} catch (IOException e) {
				System.err.println( "Error while parsing the json document" );
				e.printStackTrace();
			}

			for (Entry<String, Object> entry : jsonAsMap.entrySet()) {
				System.out.println( entry.getKey() + ": " + entry.getValue() );
			}
			System.err.println( "ID_str " + ((HashMap<String, Object>)jsonAsMap.get( "_source" )).get( "id_str" ));

			System.out.println( "Proceso: " + new DecimalFormat( "#0.0000" )
					.format( ( (double) ( i / 10000.0 ) ) * 100 ) );
		}
	}
	
	@Test
	public void testElasticConection() throws Exception {
		ElasticSearch es = new ElasticSearchConnection("elasticsearch", 9300);
		es.connect();
		System.out.println(es.getClient());
		es.getClient().close();
	}
	
	@Test
	public void indexTest() throws Exception {
		
		ElasticSearch es = new ElasticSearchConnection("elasticsearch", 9300);
		es.connect();
		System.out.println(es.getClient());
	
		System.out.println("indexing...");
		
		FileToLines ftl = new FileToLines( "2008-Feb-02-04-EN.json" );
		for (String str : ftl.lines()) {
			es.indexDocument(str);
		}
		System.out.println("done...");
		
		es.getClient().close();
	}

}
