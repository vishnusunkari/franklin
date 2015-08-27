package edu.franklin.eac.model.passwordreset;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Questions implements Serializable {
	private Integer qid;
	private String question;
	public Questions() {
	}
	/**
	 * @param question
	 */
	public Questions(String question) {
		this.question = question;
	}
	/**
	 * @param qid
	 */
	public Questions(Integer qid) {
		this.qid = qid;
	}
	/**
	 * @return the qid
	 */
	public Integer getQid() {
		return qid;
	}
	/**
	 * @param qid the qid to set
	 */
	public void setQid(Integer qid) {
		this.qid = qid;
	}
	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}
	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}
	/**
	 * TODO
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @param obj
	 * @return
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Questions) {
			Questions q = (Questions)obj;
			if(q.getQid() != null && getQid() != null)
				return getQid().equals(q.getQid());
			return getQuestion().equalsIgnoreCase(q.getQuestion());
		}
		return super.equals(obj);
	}
}
	


