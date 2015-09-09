package moe.mzry.ilmare;

import android.app.*;
import android.test.*;
import android.util.Pair;

import moe.mzry.ilmare.service.data.FirebaseLatLng;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
  public ApplicationTest() {
    super(Application.class);
  }

  public void testRelativePosition() {
    FirebaseLatLng origin = new FirebaseLatLng(0, 0); // somewhere in central west africa
    FirebaseLatLng england = new FirebaseLatLng(45, 0); // maybe...
    Pair<Double, Double> dis = england.relativeDistance(origin);
    assertTrue(dis.first > 0);
    assertTrue(Math.abs(dis.second) < 1e-8);

    FirebaseLatLng singapore = new FirebaseLatLng(0, 115); // maybe...
    dis = singapore.relativeDistance(origin);
    assertTrue("Expected 0, got: " + dis.first, Math.abs(dis.first) < 1e-8);
    assertTrue(dis.second < 0);

  }
}