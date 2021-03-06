package grammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/** 
 * 言葉の単位のうち、下位要素を持つもの.
 * 文章({@code Writing})、段落({@code Paragraph})、文({@code Sentence})、文節({@code Clause})、単語({@code Word})が対象.
 * @author tanabekentaro
 *
 * @param <C> 下位要素の型
 */
public abstract class SyntacticParent<C extends SyntacticChild> implements GrammarInterface {
	
	protected final List<C> children;

	/* ================================================== */
	/* =================== Constructor ================== */
	/* ================================================== */
	public SyntacticParent(List<C> children) {
		this.children = new ArrayList<>(children);
	}


	/* ================================================== */
	/* ================== Member Method ================= */
	/* ================================================== */
	public boolean replace(C before, C after) {
		int beforeIndex = children.indexOf(before);
		if (beforeIndex == -1)
			return false;
		return before == children.set(beforeIndex, after);
	}

	/** 
	 * 指定の子要素のリストがこの親要素に順番を維持して含まれているか.
	 * @param subChildren
	 * @return 子要素が順番通りに含まれている場合、{@code true}
	 */
	public boolean containsChildren(List<C> subChildren) {
		int size = children.size();
		int subsize = subChildren.size();
		for (int i = 0; i < size - subsize; i++) {
			int fromIndex = i, toIndex = i + subsize;
			if (subChildren.equals(children.subList(fromIndex, toIndex)))
				return true;
		}
		return false;
	}
	public boolean remove(C o) {
		return children.remove(o);
	}
	public List<C> subList(int fromIndex, int toIndex) {
		return children.subList(fromIndex, toIndex);
	}
	public int indexOfChild(C predicate) {
		return children.indexOf(predicate);
	}

	public List<Integer> indexesOfChildren(List<C> clauseList) {
		return clauseList.stream().map(this::indexOfChild).collect(Collectors.toList());
	}

	public C nextChild(C clause) {
		int nextIndex = indexOfChild(clause) + 1;
		if (nextIndex <= 0 || children.size() <= nextIndex)
			return null;
		return children.get(nextIndex);
	}

	public C previousChild(C clause) {
		int prevIndex = indexOfChild(clause) - 1;
		if (prevIndex < 0)
			return null;
		return children.get(prevIndex);
	}

	public C head() {
		return children.isEmpty() ? null : children.get(0);
	}

	public C tail() {
		return children.isEmpty() ? null : children.get(children.size() - 1);
	}

	public <P extends SyntacticParent<C>> boolean idEquals(P object) {
		return this.id() == object.id();
	}
	/* ================================================== */
	/* ================= Interface Method =============== */
	/* ================================================== */	
	public List<C> getChildren() {
		return children;
	}
	
	/* ================================================== */
	/* ================== Object Method ================= */
	/* ================================================== */
	@Override
	public int hashCode() {
		return children.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SyntacticParent))
			return false;
		SyntacticParent<?> other = SyntacticParent.class.cast(obj);
		return Objects.equals(this.getChildren(), other.getChildren());
	}
	
	@Override
	public String toString() {
		return children.stream()
				.map(C::toString)
				.collect(Collectors.joining());
	}

}
