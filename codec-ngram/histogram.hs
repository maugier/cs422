import Data.List
import Control.Arrow

main = putStr "histogram = " >> getContents >>= print . map (head &&& length) . group . sort . concat . lines
