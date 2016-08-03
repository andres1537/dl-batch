# ------------------------------------- #
# Run Batch 							#
# ------------------------------------- #
#!/bin/bash
java -Xms512m -Xmx4096m -jar /usr/local/batch/dl-batch/lib/dl-batch.jar -n BatchDBLP -f /usr/local/data/batchDBLP/dblp.xml &
MyPID=$!
echo $MyPID