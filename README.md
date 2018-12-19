## First glance

![showcase](/showcase.gif)

## Add Dependencies

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.enix223:QActionSheet:1.0.0'
}
```

## Usage

```java
ArrayList<String> choices = new ArrayList<>();
choices.add("Choice 1");
choices.add("Choice 2");
choices.add("Choice 3");
        
QActionSheet actionSheet = new QActionSheet.Builder(DemoActivity.this)
                                           .data(choices)
                                           .initialSelection(currentChoice)
                                           .title("Select a choice")
                                           .onItemSelectListener(new QActionSheet.OnItemSelectListener() {
                                                @Override
                                                public void onItemSelected(int index, String item) {
                                                    currentChoice = index;
                                                    Log.e(TAG, String.format("Select item: %d, %s", index, item));
                                                }
                                           })
                                           .build();

actionSheet.show();
```
                
