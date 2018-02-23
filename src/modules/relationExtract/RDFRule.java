package modules.relationExtract;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import data.RDF.Namespace;
import modules.Generator;

public class RDFRule {
	/**
	 * キーが変数名あるいはRDFノードの省略名
	 * 値が代入されるリソースのURI
	 */
	private Map<String, String> varURIMap;

	private RDFGraphPattern ifPattern;
	private RDFGraphPattern thenPattern;

	/***********************************/
	/**********  Constructor  **********/
	/***********************************/
	public RDFRule(String[][] ifs, String[][] thens) {
		this.varURIMap = Stream.concat(Stream.of(ifs), Stream.of(thens))
				.flatMap(Stream::of)
				.map(k -> {
					String[] ns = k.split(":");
					String v = (ns.length == 2  && !k.startsWith("<") && !k.startsWith("\""))? 
						v = Namespace.getURIFromPrefix(ns[0]) + ns[1] : k;
					return new SimpleEntry<>(k, v);
				})
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (k1, k2) -> k1));
		this.ifPattern = new RDFGraphPattern(
				Stream.of(ifs)
				.map(tri -> new RDFTriplePattern(tri[0], tri[1], tri[2]))
				.collect(Collectors.toSet()));
		this.thenPattern = new RDFGraphPattern(
				Stream.of(thens)
				.map(tri -> new RDFTriplePattern(tri[0], tri[1], tri[2]))
				.collect(Collectors.toSet()));
	}


	/***********************************/
	/**********  MemberMethod **********/
	/***********************************/
	public Model expands(Model targetModel) {
		Model m = solve(targetModel);
		
		//TODO
		/*
		new Generator().convertJena2Original(m).stream()
		.map(tri -> tri.toString()).forEach(System.out::println);
		System.out.println();
		*/
		
		targetModel.add(m);
		return targetModel;
	}

	public Model converts(Model targetModel) {
		return solve(targetModel);
	}

	private Model solve(Model targetModel) {
		Model model = ModelFactory.createDefaultModel();
		QueryExecution qexec = QueryExecutionFactory.create(ifPattern.toQuery(), targetModel);
		ResultSet resultSet = qexec.execSelect();
		List<String> varNames = resultSet.getResultVars();
		
		while (resultSet.hasNext()) {
			QuerySolution qsol = resultSet.next();
			varNames.stream().forEach(s -> varURIMap.put("?"+s, qsol.get(s).toString()));

			thenPattern.getTriplePatterns().stream()
				.map(tp -> tp.fillStatement(targetModel, varURIMap))
				.forEach(model::add);
		}
		return model;
	}

	/**********************************/
	/********** Objectメソッド **********/
	/**********************************/
	@Override
	public String toString() {
		return "IF "+ ifPattern.toString() +"\nTHEN "+ thenPattern.toString();
	}
}