package data.id;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import data.RDF.MyJenaModel;
import grammar.Sentence;

public class SentenceIDMap extends IDLinkedMap<Sentence> {
	private static final long serialVersionUID = -2957160502289250254L;
	

	
	/****************************************/
	/**********   Static  Method   **********/
	/****************************************/
	public static SentenceIDMap createFromList(List<Sentence> sentenceList) {
		LinkedHashMap<Sentence, IDTuple> lhm = sentenceList.stream()
				.collect(Collectors.toMap(s -> s, s -> new IDTuple(), (e1, e2) -> e1, LinkedHashMap::new));
		return new SentenceIDMap(lhm);
	}
	
	/****************************************/
	/**********     Constructor    **********/
	/****************************************/
	public SentenceIDMap() {
		super();
	}
	public SentenceIDMap(int initialCapacity) {
		super(initialCapacity);
	}
	public SentenceIDMap(LinkedHashMap<Sentence, IDTuple> m) {
		super(m);
	}



	/****************************************/
	/**********   Member  Method   **********/
	/****************************************/
	public void setLongSentenceID() {
		forEach((k, v) -> v.setLongSentenceID(k.id));
	}
	public void setShortSentenceID() {
		forEach((k, v) -> v.setShortSentenceID(k.id));
	}
	
	public List<String> stringList() {
		return keySet().stream().map(Sentence::name).collect(Collectors.toList());
	}
	public SentenceIDMap replaceSentence2Sentences(Map<Sentence, List<Sentence>> replaceMap) {
		SentenceIDMap sm = new SentenceIDMap();
		replaceMap.forEach((st, sts) ->
		sts.forEach(s -> sm.put(s, get(st).clone()))
		);
		return sm;
	}
	public ModelIDMap replaceSentence2Model(Map<Sentence, MyJenaModel> replaceMap) {
		ModelIDMap mm = new ModelIDMap();
		replaceMap.forEach((st, md) -> mm.put(md, get(st).clone()));
		return mm;
	}
	
}