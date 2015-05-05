package fr.tse.fi2.hpp.labs.queries.impl.Lab4;

import java.util.ArrayList;
import java.util.List;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class MembershipQuery extends AbstractQueryProcessor {

	private List<DebsRecord> recs;

	public MembershipQuery(QueryProcessorMeasure measure) {
		super(measure);
		recs = new ArrayList<>();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void process(DebsRecord record) {
		// TODO Auto-generated method stub
		recs.add(record);
	}

	public List<DebsRecord> getRecs() {
		return recs;
	}

	public void setRecs(List<DebsRecord> recs) {
		this.recs = recs;
	}

	public boolean Search(float longS, float latS, float longE, float latE,
			String taxi) {
		for (int i = 0; i < recs.size(); i++) {
			DebsRecord a = recs.get(i);
			if ((a.getPickup_longitude() == longS)
					&& (a.getPickup_latitude() == latS)
					&& (a.getDropoff_longitude() == longE)
					&& (a.getDropoff_latitude() == latE)
					&& (a.getHack_license().equals(taxi))) {
				return true;
			}
		}
		return false;
	}
}