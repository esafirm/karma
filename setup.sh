
#!/bin/bash
rm settings.gradle

LIST_OF_MODULES=(
    'karma-core'
    'karma-renderer'
    'karma-timetravel'
    'karma-timetravel-dashboard'
)

for mod in ${LIST_OF_MODULES[@]}; do
    echo "include '$mod'" >> settings.gradle
done

cat settings.gradle