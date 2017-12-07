package restaurant.util;

import java.util.Comparator;

import restaurant.service.Table;

public class TableSortingComparator implements Comparator<Table> {
    @Override
    public int compare(Table table1, Table table2) {
        return table1.getTableSize() - table2.getTableSize();
    }
}
