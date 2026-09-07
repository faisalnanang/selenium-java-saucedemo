const fs = require('node:fs');
const path = require('node:path');

const outputPath = path.resolve(__dirname, '..', 'assets', 'ogya.ico');
fs.mkdirSync(path.dirname(outputPath), { recursive: true });

const sizes = [16, 32, 48];
const images = sizes.map((size) => {
  const pixels = Buffer.alloc(size * size * 4);
  let offset = 0;

  for (let y = size - 1; y >= 0; y -= 1) {
    for (let x = 0; x < size; x += 1) {
      const distance = Math.abs(x - (size - 1) / 2) + Math.abs(y - (size - 1) / 2);
      const isDiamond = distance < size * 0.36;
      const isCenter = Math.abs(x - (size - 1) / 2) < size * 0.1 && Math.abs(y - (size - 1) / 2) < size * 0.1;
      const [red, green, blue, alpha] = isCenter
        ? [255, 255, 255, 255]
        : isDiamond
          ? [255, 153, 51, 255]
          : [30, 64, 175, 255];

      pixels[offset++] = blue;
      pixels[offset++] = green;
      pixels[offset++] = red;
      pixels[offset++] = alpha;
    }
  }

  const header = Buffer.alloc(40);
  header.writeUInt32LE(40, 0);
  header.writeInt32LE(size, 4);
  header.writeInt32LE(size * 2, 8);
  header.writeUInt16LE(1, 12);
  header.writeUInt16LE(32, 14);
  header.writeUInt32LE(pixels.length, 20);

  const maskRowSize = Math.ceil(size / 32) * 4;
  return Buffer.concat([header, pixels, Buffer.alloc(maskRowSize * size)]);
});

const directoryHeader = Buffer.from([0, 0, 1, 0, sizes.length, 0]);
const entries = [];
let imageOffset = directoryHeader.length + sizes.length * 16;

for (const [index, image] of images.entries()) {
  const entry = Buffer.alloc(16);
  entry.writeUInt8(sizes[index], 0);
  entry.writeUInt8(sizes[index], 1);
  entry.writeUInt16LE(1, 4);
  entry.writeUInt16LE(32, 6);
  entry.writeUInt32LE(image.length, 8);
  entry.writeUInt32LE(imageOffset, 12);
  entries.push(entry);
  imageOffset += image.length;
}

fs.writeFileSync(outputPath, Buffer.concat([directoryHeader, ...entries, ...images]));
console.log(`Created ${outputPath}`);
