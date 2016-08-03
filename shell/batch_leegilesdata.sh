# ------------------------------------- #
# Run Batch 							#
# ------------------------------------- #
#!/bin/bash
java -Xms512m -Xmx4096m -jar /usr/local/batch/dl-batch/lib/dl-batch.jar -n BatchLeeGilesData -f /usr/local/data/batchLeeGilesData/AGupta.txt,/usr/local/data/batchLeeGilesData/AKumar.txt,/usr/local/data/batchLeeGilesData/CChen.txt,/usr/local/data/batchLeeGilesData/DJohnson.txt,/usr/local/data/batchLeeGilesData/JLee.txt,/usr/local/data/batchLeeGilesData/JMartin.txt,/usr/local/data/batchLeeGilesData/JRobinson.txt,/usr/local/data/batchLeeGilesData/JSmith.txt,/usr/local/data/batchLeeGilesData/KTanaka.txt,/usr/local/data/batchLeeGilesData/MBrown.txt,/usr/local/data/batchLeeGilesData/MJones.txt,/usr/local/data/batchLeeGilesData/MMiller.txt,/usr/local/data/batchLeeGilesData/SLee.txt,/usr/local/data/batchLeeGilesData/YChen.txt &
MyPID=$!
echo $MyPID