
package scalaSci.jplot


import scala.collection.mutable.ArrayBuffer;

/**
 * An XY dataset consisting of some number of named series, each consisting
 * of items of type Item, with an associated x value, y value, and optionally
 * a name and tooltip.
 *
 * @author dramage
 */
class XYDataset[Item](x : Item=>Number, y : Item=>Number, label : Item=>String, tip : Item=>String)
extends org.jfree.data.xy.AbstractXYDataset {
  val names = ArrayBuffer[String]();
  val items = ArrayBuffer[IndexedSeq[Item]]();

  override def getSeriesKey(series : Int) =
    names(series)

  override def getSeriesCount =
    names.length

  override def getItemCount(series : Int) =
    items(series).length

  override def getX(series : Int, item : Int) : Number =
    x(items(series)(item))

  override def getY(series : Int, item : Int) : Number =
    y(items(series)(item))

  def getLabel(series : Int, item : Int) : String =
    label(items(series)(item))

  def getTip(series : Int, item : Int) : String =
    tip(items(series)(item))


}

object XYDataset {
  def apply[Item](name : String, items : IndexedSeq[Item], x : Item=>Number, y : Item=>Number, label : Item=>String, tip : Item=>String)
  : XYDataset[Item] = {
    val rv = new XYDataset(x, y, label, tip);
    rv.names += name;
    rv.items += items;
    rv;
  }
}

/**
 * An XYX dataset consisting of some number of named series, each consisting
 * of items of type Item, with an associated x value, y value, z value,
 * and optionally a name and tooltip.
 *
 * @author dramage
 */
class XYZDataset[Item](x : Item=>Number, y : Item=>Number, z : Item=>Number, label : Item=>String, tip : Item=>String)
extends org.jfree.data.xy.AbstractXYZDataset {
  val names = ArrayBuffer[String]();
  val items = ArrayBuffer[IndexedSeq[Item]]();

  override def getSeriesKey(series : Int) =
    names(series);

  override def getSeriesCount =
    names.length;

  override def getItemCount(series : Int) =
    items(series).length;

  override def getX(series : Int, item : Int) : Number =
    x(items(series)(item));

  override def getY(series : Int, item : Int) : Number =
    y(items(series)(item));

  override def getZ(series : Int, item : Int) : Number =
    z(items(series)(item));

  def getLabel(series : Int, item : Int) : String =
    label(items(series)(item));

  def getTip(series : Int, item : Int) : String =
    tip(items(series)(item));
}

object XYZDataset {
  def apply[Item](name: String, items: IndexedSeq[Item], x: Item => Number, y: Item => Number, z: Item => Number, label: Item => String, tip: Item => String)
  : XYZDataset[Item] = {
    val rv = new XYZDataset(x, y, z, label, tip);
    rv.names += name;
    rv.items += items;
    rv;
  }
}
