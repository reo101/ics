import { KeyValue } from "@angular/common";
import { Pipe } from "@angular/core";

@Pipe({
  name: "sortByValue"
})
export class SortByValuePipe<K, V> {
  transform(array: Array<KeyValue<K, V>>, ascending: Boolean = false): Array<KeyValue<K, V>> {
    array.sort((a: KeyValue<K, V>, b: KeyValue<K, V>) => {
      return (a.value == b.value
        ? 0
        : ((a.value > b.value)
          ? 1
          : -1) * (ascending ? 1 : -1))
    });

    return array;
  }
}
