package grammar.morpheme;

import java.util.List;
import java.util.Objects;

import grammar.GrammarInterface;
import grammar.Identifiable;
import util.UniqueSet;
import util.Uniqueness;

public class EnMorpheme implements GrammarInterface, Identifiable, Uniqueness<EnMorpheme> {
	private static UniqueSet<EnMorpheme> uniqueset = new UniqueSet<>(100);
	
	protected final int id;			// 通し番号
	protected final String name;		// 形態素の文字列
	protected final Tags tags;		// 品詞リスト
	
	

	/***********************************/
	/**********  Constructor  **********/
	/***********************************/
	/**
	 * 全てこのコンストラクタに行き着くようにすること．
	 * @param name
	 * @param tags
	 */
	private EnMorpheme(String name, Tags tags) {
		this.id = uniqueset.size();
		this.name = name;
		this.tags = tags;
		
		EnMorpheme.uniqueset.add(this);
	}
	protected EnMorpheme(String name, List<String> tagList) {
		this(name, new Tags(tagList));
	}
	protected EnMorpheme(List<String> name_tags) {
		this(name_tags.get(0), name_tags.subList(1, name_tags.size()));
	}
	
	
	public static EnMorpheme getOrNewInstance(String name, List<String> tags) {
		EnMorpheme em = new EnMorpheme(name, tags);
		return uniqueset.getExistingOrIntact(em);
	}
	public static EnMorpheme getOrNewInstance(List<String> name_tags) {
		EnMorpheme em = new EnMorpheme(name_tags);
		return uniqueset.getExistingOrIntact(em);
	}
	
	
	/***********************************/
	/**********   Interface   **********/
	/***********************************/
	public void printDetail() {
		System.out.println(name);
	}
	public int getID() {
		return id;
	}
	public int compareTo(EnMorpheme o) {
		int comparison = name.compareTo(o.name);
		return comparison!=0? comparison : tags.compareTo(o.tags);
	}

	
	/**********************************/
	/**********    Getter    **********/
	/**********************************/
	public Tags getTags() {
		return tags;
	}
	public String getName() {
		return name;
	}
	/**********   準Getter   **********/
	public List<String> tags() {
		return tags.getTagList().subList(0, Tags.CONJUGATION+1);
	}
	public String mainPoS() {
		return tags.mainPoS();
	}
	public String subPoS1() {
		return tags.subPoS1();
	}
	public String subPoS2() {
		return tags.subPoS2();
	}
	public String subPoS3() {
		return tags.subPoS3();
	}
	public String inflection() {
		return tags.inflection();
	}
	public String conjugation() {
		return tags.conjugation();
	}

	
	
	/**********************************/
	/********** Objectメソッド **********/
	/**********************************/
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnMorpheme other = (EnMorpheme) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return Objects.toString(name, "nullMorpheme");
	}
}