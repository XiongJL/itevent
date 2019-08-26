package com.liwinon.itevent.entity.primary;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ITE_EventType")
public class EventType {
	 	@Id
	 	@GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int eTypeId;
	    private String level_1;
	    private String level_2;
	    private String description;
	    private String team;
		private int startLevel;
		public int geteTypeId() {
			return eTypeId;
		}
		public void seteTypeId(int eTypeId) {
			this.eTypeId = eTypeId;
		}
		public String getLevel_1() {
			return level_1;
		}
		public void setLevel_1(String level_1) {
			this.level_1 = level_1;
		}
		public String getLevel_2() {
			return level_2;
		}
		public void setLevel_2(String level_2) {
			this.level_2 = level_2;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public int getStartLevel() {
		return startLevel;
	}

	public void setStartLevel(int startLevel) {
		this.startLevel = startLevel;
	}

	public EventType() {
	}

	public EventType(String level_1, String level_2, String description, String team, int startLevel) {
		this.level_1 = level_1;
		this.level_2 = level_2;
		this.description = description;
		this.team = team;
		this.startLevel = startLevel;
	}

	@Override
	public String toString() {
		return "EventType{" +
				"eTypeId=" + eTypeId +
				", level_1='" + level_1 + '\'' +
				", level_2='" + level_2 + '\'' +
				", description='" + description + '\'' +
				", team='" + team + '\'' +
				", startLevel=" + startLevel +
				'}';
	}
}
